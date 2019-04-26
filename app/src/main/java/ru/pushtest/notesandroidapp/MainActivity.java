package ru.pushtest.notesandroidapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.pushtest.notesandroidapp.database.DatabaseHelper;
import ru.pushtest.notesandroidapp.database.EditActivity;
import ru.pushtest.notesandroidapp.database.model.Note;
import ru.pushtest.notesandroidapp.database.utils.MyDividerItemDecoration;
import ru.pushtest.notesandroidapp.database.utils.RecyclerTouchListener;
import ru.pushtest.notesandroidapp.database.view.NotesAdapter;

public class MainActivity extends AppCompatActivity {
	
	private NotesAdapter mAdapter;
	private List< Note > notesList = new ArrayList<>();
	private CoordinatorLayout coordinatorLayout;
	private RecyclerView recyclerView;
	private TextView noNotesView;
	
	private DatabaseHelper db;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		coordinatorLayout = findViewById(R.id.coordinator_layout);
		recyclerView = findViewById(R.id.recycler_view);
		noNotesView = findViewById(R.id.empty_notes_view);
		
		db = new DatabaseHelper(this);
		
		notesList.addAll(db.getAllNotes());
		
		
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				showNoteDialog(false, null, -1);
				Intent intent = new Intent( MainActivity.this, EditActivity.class );
				startActivity( intent );
			}
		});
		
		mAdapter = new NotesAdapter(this, notesList);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
		recyclerView.setAdapter(mAdapter);
		
		toggleEmptyNotes();
		
		recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
				recyclerView, new RecyclerTouchListener.ClickListener() {
			@Override
			public void onClick(View view, final int position) {
			}
			
			@Override
			public void onLongClick(View view, int position) {
				showActionsDialog(position);
			}
		}));
	}
	
	private void createNote(String title, String note) {
		long id = db.insertNote(title, note);
		
		Note n = db.getNote(id);
		
		if (n != null) {
			notesList.add(0, n);
			
			mAdapter.notifyDataSetChanged();
			
			toggleEmptyNotes();
		}
	}
	
	private void updateNote(String note, int position) {
		Note n = notesList.get(position);
		n.setNote(note);
		
		db.updateNote(n);
		
		notesList.set(position, n);
		mAdapter.notifyItemChanged(position);
		
		toggleEmptyNotes();
	}
	
	private void deleteNote(int position) {
		// deleting the note from db
		db.deleteNote(notesList.get(position));
		
		// removing the note from the list
		notesList.remove(position);
		mAdapter.notifyItemRemoved(position);
		
		toggleEmptyNotes();
	}
	
	private void showActionsDialog(final int position) {
		CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose option");
		builder.setItems(colors, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					showNoteDialog(true, notesList.get(position), position);
				} else {
					deleteNote(position);
				}
			}
		});
		builder.show();
	}
	
	private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
		LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
		View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);
		
		AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
		alertDialogBuilderUserInput.setView(view);
		
		final EditText inputTitle = view.findViewById( R.id.note_title );
		final EditText inputNote = view.findViewById(R.id.note);
		TextView dialogTitle = view.findViewById(R.id.dialog_title);
		dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));
		
		if (shouldUpdate && note != null) {
			inputNote.setText(note.getNote());
		}
		alertDialogBuilderUserInput
				.setCancelable(false)
				.setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogBox, int id) {
					
					}
				})
				.setNegativeButton("cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogBox, int id) {
								dialogBox.cancel();
							}
						});
		
		final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
		alertDialog.show();
		
		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( TextUtils.isEmpty(inputNote.getText().toString())) {
					Toast.makeText(MainActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
					return;
				} else {
					alertDialog.dismiss();
				}
				
				if (shouldUpdate && note != null) {
					updateNote(inputNote.getText().toString(), position);
				} else {
					createNote(inputTitle.getText().toString(), inputNote.getText().toString());
				}
			}
		});
	}
	
	private void toggleEmptyNotes() {
		
		if (db.getNotesCount() > 0) {
			noNotesView.setVisibility(View.GONE);
		} else {
			noNotesView.setVisibility(View.VISIBLE);
		}
	}
	
	
}
