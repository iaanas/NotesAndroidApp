package ru.pushtest.notesandroidapp;

import android.content.Context;
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
import android.widget.RadioGroup;
import android.widget.SeekBar;
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
import ru.pushtest.notesandroidapp.notifications.NotificationHelper;

public class MainActivity extends AppCompatActivity {
	
	private NotesAdapter mAdapter;
	private List< Note > notesList = new ArrayList<>();
	private CoordinatorLayout coordinatorLayout;
	private RecyclerView recyclerView;
	private TextView noNotesView;
	
	private DatabaseHelper db;
	
	private String hours;
	private String minutes;
	private Context mContext;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		coordinatorLayout = findViewById(R.id.coordinator_layout);
		recyclerView = findViewById(R.id.recycler_view);
		noNotesView = findViewById(R.id.empty_notes_view);
		
		
		db = new DatabaseHelper(this);
		
		notesList.addAll(db.getAllNotes());
		
		
		FloatingActionButton fab = findViewById(R.id.fab);
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
	
	private void createNote(String title, String note, String priority, String progress, String hours, String minutes) {
		long id = db.insertNote(title, note, priority, progress, hours, minutes);
		
		Note n = db.getNote(id);
		
		if (n != null) {
			notesList.add(0, n);
			
			mAdapter.notifyDataSetChanged();
			
			toggleEmptyNotes();
		}
	}
	
	private void updateNote(String note, int position, String title, String priority, String progress, String hours, String minutes) {
		Note n = notesList.get(position);
		n.setNote(note);
		n.setTitle( title );
		n.setPriority( priority );
		n.setProgress( progress );
		n.setHours( hours );
		n.setMinutes( minutes );
		
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
		CharSequence colors[] = new CharSequence[]{"Редактировать", "Удалить"};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Выберите действие");
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
		
		final EditText inputTitle = view.findViewById( R.id.dialog_title );
		final EditText inputNote = view.findViewById(R.id.note);
		final EditText inputPriority = view.findViewById( R.id.priority_result );
		final TextView selection = view.findViewById( R.id.selection );
		final TextView progresstext = view.findViewById( R.id.progress_edit );
		final SeekBar seekBar = view.findViewById( R.id.seekBar );
		
		final EditText hoursView = view.findViewById( R.id.alarm_hours );
		final EditText minutesView = view.findViewById( R.id.alarm_minutes );
		
		
		final RadioGroup radGr = view.findViewById( R.id.radioGroup );
		radGr.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener( ) {
			@Override
			public void onCheckedChanged( RadioGroup arg0 , int id ) {
				
				switch ( id ){
					case R.id.high:
						selection.setText( "Высокий" );
						break;
					case R.id.middle:
						selection.setText( "Средний" );
						break;
					case R.id.low:
						selection.setText( "Низкий" );
						break;
					default:
						break;
				}
			}
		} );
		
		seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener( ) {
			@Override
			public void onProgressChanged( SeekBar seekBar , int progress , boolean fromUser ) {
				progresstext.setText( String.valueOf( seekBar.getProgress() ) );
			}
			
			@Override
			public void onStartTrackingTouch( SeekBar seekBar ) {
			
			}
			
			@Override
			public void onStopTrackingTouch( SeekBar seekBar ) {
			
			}
		} );
		
		
		TextView dialogTitle = view.findViewById(R.id.dialog_title);
//		dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));
		dialogTitle.setText( note.getTitle());
		
		if (shouldUpdate && note != null) {
			inputNote.setText(note.getNote());
		}
		alertDialogBuilderUserInput
				.setCancelable(false)
				.setPositiveButton(shouldUpdate ? "обновить" : "сохранить", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogBox, int id) {
					
					}
				})
				.setNegativeButton("закрыть",
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
					Toast.makeText(MainActivity.this, "Опишите задачу!", Toast.LENGTH_SHORT).show();
					return;
				} else {
					alertDialog.dismiss();
				}
				
				if (shouldUpdate && note != null) {
					hours = hoursView.getText().toString();
					minutes = minutesView.getText().toString();
					updateNote(inputNote.getText().toString(), position, inputTitle.getText().toString(), selection.getText().toString()
					, progresstext.getText().toString(), hoursView.getText().toString(), minutesView.getText().toString());
					NotificationHelper.scheduleRepeatingRTCNotification(MainActivity.this, hoursView.getText().toString(), minutesView.getText().toString());
					NotificationHelper.enableBootReceiver(MainActivity.this);
				} else {
					createNote(inputTitle.getText().toString(), inputNote.getText().toString(), selection.getText().toString()
					, progresstext.getText().toString(), hoursView.getText().toString(), minutesView.getText().toString());
					NotificationHelper.scheduleRepeatingRTCNotification(MainActivity.this, hoursView.getText().toString(), minutesView.getText().toString());
					NotificationHelper.enableBootReceiver(MainActivity.this);
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
	
//	public void clickToggleButtonRTC(View view) {
//		boolean isEnabled = ((ToggleButton)view).isEnabled();
//
//		if (isEnabled) {
//			NotificationHelper.scheduleRepeatingRTCNotification(mContext, hours, minutes);
//			NotificationHelper.enableBootReceiver(mContext);
//		} else {
//			NotificationHelper.cancelAlarmRTC();
//			NotificationHelper.disableBootReceiver(mContext);
//		}
//	}
//
//	public void clickToggleButtonElapsed(View view) {
//		boolean isEnabled = ((ToggleButton)view).isEnabled();
//
//		if (isEnabled) {
//			NotificationHelper.scheduleRepeatingElapsedNotification(mContext);
//			NotificationHelper.enableBootReceiver(mContext);
//		} else {
//			NotificationHelper.cancelAlarmElapsed();
//			NotificationHelper.disableBootReceiver(mContext);
//		}
//	}
//
//	public void cancelAlarms(View view) {
//		NotificationHelper.cancelAlarmRTC();
//		NotificationHelper.cancelAlarmElapsed();
//		NotificationHelper.disableBootReceiver(mContext);
//	}
	
	
}
