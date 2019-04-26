package ru.pushtest.notesandroidapp.database;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.pushtest.notesandroidapp.MainActivity;
import ru.pushtest.notesandroidapp.R;
import ru.pushtest.notesandroidapp.database.model.Note;
import ru.pushtest.notesandroidapp.database.view.NotesAdapter;

public class EditActivity extends AppCompatActivity {
	
	private DatabaseHelper db;
	private NotesAdapter mAdapter;
	private List< Note > notesList = new ArrayList<>();
	private CoordinatorLayout coordinatorLayout;
	private RecyclerView recyclerView;
	private TextView noNotesView;
	
	private EditText titleNewNote;
	private EditText newNote;
	
	private Button saveNote;
	
	private ImageButton redButton;
	private ImageButton yellowButton;
	private ImageButton greenButton;
	
	public int count;
	public static int countTotal;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_edit );
		
		titleNewNote = (EditText ) findViewById( R.id.edt_title );
		newNote = (EditText) findViewById( R.id.edt_body_note );
		saveNote = (Button ) findViewById( R.id.btn_save_note );
		
		db = new DatabaseHelper(this);
		mAdapter = new NotesAdapter(this, notesList);
		noNotesView = findViewById(R.id.empty_notes_view);
		
		redButton = ( ImageButton ) findViewById( R.id.btn_red );
		yellowButton = (ImageButton ) findViewById( R.id.btn_yellow );
		greenButton = (ImageButton ) findViewById( R.id.btn_green );
		
		redButton.setOnClickListener( new View.OnClickListener( ) {
			@Override
			public void onClick( View v ) {
				redButton.setImageResource( R.drawable.b_red );
				yellowButton.setImageResource( R.drawable.c_yellow );
				greenButton.setImageResource( R.drawable.e_green );
				count = 0;
			}
		} );
		
		yellowButton.setOnClickListener( new View.OnClickListener( ) {
			@Override
			public void onClick( View v ) {
				yellowButton.setImageResource( R.drawable.d_yellow );
				redButton.setImageResource( R.drawable.a_reb );
				greenButton.setImageResource( R.drawable.e_green );
				count = 1;
			}
		} );
		
		greenButton.setOnClickListener( new View.OnClickListener( ) {
			@Override
			public void onClick( View v ) {
				yellowButton.setImageResource( R.drawable.c_yellow );
				redButton.setImageResource( R.drawable.a_reb );
				greenButton.setImageResource( R.drawable.g_green );
				count = 2;
			}
		} );
		
		
		saveNote.setOnClickListener( new View.OnClickListener( ) {
			@Override
			public void onClick( View v ) {
				createNote( titleNewNote.getText().toString(), newNote.getText().toString() );
				Intent intent = new Intent( EditActivity.this, MainActivity.class );
				
				countTotal = count;
				Toast.makeText( EditActivity.this, "Count: "+countTotal, Toast.LENGTH_SHORT ).show();
				
				startActivity( intent );
			}
		} );
		
	}
	
	private void createNote(String title, String note) {
		long id = db.insertNote(title, note);
		
		Note n = db.getNote(id);
		
		if (n != null) {
			notesList.add(0, n);
			
			mAdapter.notifyDataSetChanged();
		}
	}
	
	public int cEnd(){
		
		return countTotal;
	}

}
