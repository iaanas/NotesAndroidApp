package ru.pushtest.notesandroidapp.database.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru.pushtest.notesandroidapp.R;
import ru.pushtest.notesandroidapp.database.EditActivity;
import ru.pushtest.notesandroidapp.database.model.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder>{
	
	
	private Context context;
	private List< Note > notesList;
	
	private EditActivity countEnd;
	private int cEnd;
	
	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup , int i ) {
		View itemView = LayoutInflater.from( viewGroup.getContext())
				.inflate( R.layout.note_list_row, viewGroup, false);
		
		countEnd = new EditActivity();
		
		return new MyViewHolder( itemView );
	}
	
	@Override
	public void onBindViewHolder( @NonNull MyViewHolder myViewHolder , int i ) {
		cEnd = EditActivity.countTotal;
		
		Note note = notesList.get( i );
		myViewHolder.title.setText( note.getTitle() );
		myViewHolder.note.setText( note.getNote() );
		
		if(cEnd == 0){
			myViewHolder.dot.setImageResource( R.drawable.a_reb );
		} else if(cEnd == 1){
			myViewHolder.dot.setImageResource( R.drawable.c_yellow );
		} else if(cEnd == 2){
			myViewHolder.dot.setImageResource( R.drawable.e_green );
		}
		
		myViewHolder.timestamp.setText( formatDate(note.getTimestamp()) );
	
	}
	
	@Override
	public int getItemCount( ) {
		return notesList.size();
	}
	
	private String formatDate (String dateStr){
		try {
			SimpleDateFormat fmt = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			Date date = fmt.parse( dateStr );
			SimpleDateFormat fmtOut = new SimpleDateFormat( "MMM d" );
			return fmtOut.format( date );
		} catch ( ParseException e ){
		
		}
		return "";
	}
	
	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView title;
		public TextView note;
		public ImageView dot;
		public TextView timestamp;
		
		public MyViewHolder( View view){
			super(view);
			title = view.findViewById( R.id.note_title );
			note = view.findViewById( R.id.note );
			dot = view.findViewById( R.id.dot );
			timestamp = view.findViewById( R.id.timestamp );
		}
	}
	
	public NotesAdapter(Context context, List<Note> notesList){
		this.context = context;
		this.notesList = notesList;
	}
	
	
}
