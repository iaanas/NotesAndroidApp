package ru.pushtest.notesandroidapp.database.model;

public class Note {
	public static final String TABLE_NAME = "notes";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_NOTE = "note";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_PRIORITY = "priority";
	public static final String COLUMN_PROGRESS = "progress";
	
	private int id;
	private String title;
	private String note;
	private String timestamp;
	private String priority;
	private String progress;
	
	
	public static final String CREATE_TABLE =
			"CREATE TABLE " + TABLE_NAME + "("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ COLUMN_TITLE + " TEXT,"
					+ COLUMN_NOTE + " TEXT,"
					+ COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
	                + COLUMN_PRIORITY  + " TEXT,"
					+ COLUMN_PROGRESS + " TEXT"
					+ ")";
	
	public Note( ) {
	}
	
	public Note( int id , String title , String note , String timestamp , String priority , String progress ) {
		this.id = id;
		this.title = title;
		this.note = note;
		this.timestamp = timestamp;
		this.priority = priority;
		this.progress = progress;
	}
	
	public int getId( ) {
		return id;
	}
	
	public void setId( int id ) {
		this.id = id;
	}
	
	public String getTitle( ) {
		return title;
	}
	
	public void setTitle( String title ) {
		this.title = title;
	}
	
	public String getNote( ) {
		return note;
	}
	
	public void setNote( String note ) {
		this.note = note;
	}
	
	public String getTimestamp( ) {
		return timestamp;
	}
	
	public void setTimestamp( String timestamp ) {
		this.timestamp = timestamp;
	}
	
	public String getPriority( ) {
		return priority;
	}
	
	public void setPriority( String priority ) {
		this.priority = priority;
	}
	
	public String getProgress( ) {
		return progress;
	}
	
	public void setProgress( String progress ) {
		this.progress = progress;
	}
}
