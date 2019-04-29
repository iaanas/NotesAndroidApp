package ru.pushtest.notesandroidapp.database;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.pushtest.notesandroidapp.MainActivity;
import ru.pushtest.notesandroidapp.notifications.NotificationHelper;
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
	public String priority;
	
	private EditText hours;
	private EditText minutes;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_edit );
		
		Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
		setSupportActionBar(mActionBarToolbar);
		
		titleNewNote = findViewById( R.id.edt_title );
		newNote = findViewById( R.id.edt_body_note );
		saveNote = findViewById( R.id.btn_save_note );
		
		db = new DatabaseHelper(this);
		mAdapter = new NotesAdapter(this, notesList);
		noNotesView = findViewById(R.id.empty_notes_view);
		
		redButton = findViewById( R.id.btn_red );
		yellowButton = findViewById( R.id.btn_yellow );
		greenButton = findViewById( R.id.btn_green );
		
		hours = findViewById( R.id.alarm_hours );
		minutes = findViewById( R.id.alarm_minutes );
		
		redButton.setOnClickListener( new View.OnClickListener( ) {
			@Override
			public void onClick( View v ) {
				redButton.setImageResource( R.drawable.b_red );
				yellowButton.setImageResource( R.drawable.c_yellow );
				greenButton.setImageResource( R.drawable.e_green );
				count = 0;
				priority = "Высокий";
			}
		} );
		
		yellowButton.setOnClickListener( new View.OnClickListener( ) {
			@Override
			public void onClick( View v ) {
				yellowButton.setImageResource( R.drawable.d_yellow );
				redButton.setImageResource( R.drawable.a_reb );
				greenButton.setImageResource( R.drawable.e_green );
				count = 1;
				priority = "Средний";
			}
		} );
		
		greenButton.setOnClickListener( new View.OnClickListener( ) {
			@Override
			public void onClick( View v ) {
				yellowButton.setImageResource( R.drawable.c_yellow );
				redButton.setImageResource( R.drawable.a_reb );
				greenButton.setImageResource( R.drawable.g_green );
				count = 2;
				priority = "Низкий";
			}
		} );
		
		
		saveNote.setOnClickListener( new View.OnClickListener( ) {
			@Override
			public void onClick( View v ) {
				createNote( titleNewNote.getText().toString(), newNote.getText().toString(), priority,
						hours.getText().toString(), minutes.getText().toString());
				Intent intent = new Intent( EditActivity.this, MainActivity.class );
				
				countTotal = count;
				NotificationHelper.scheduleRepeatingRTCNotification(EditActivity.this, hours.getText().toString(), minutes.getText().toString());
				NotificationHelper.enableBootReceiver(EditActivity.this);
				
				startActivity( intent );
			}
		} );
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.toolbar_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_close:
				Intent intent = new Intent( EditActivity.this, MainActivity.class );
				startActivity( intent );
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void createNote( String title , String note , String priority, String hours, String minutes ) {
		long id = db.insertNote(title, note, priority, "0", hours, minutes );
		
		Note n = db.getNote(id);
		
		if (n != null) {
			notesList.add(0, n);
			
			mAdapter.notifyDataSetChanged();
		}
	}
	
	public int cEnd(){
		
		return countTotal;
	}
	
//	public void clickToggleButtonRTC(View view) {
//		boolean isEnabled = ((ToggleButton)view).isEnabled();
//
//		if (isEnabled) {
//			NotificationHelper.scheduleRepeatingRTCNotification(mContext, hours.getText().toString(), minutes.getText().toString());
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
