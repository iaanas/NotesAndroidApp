package ru.pushtest.notesandroidapp.database.model;

public class Note {
	public static final String TABLE_NAME = "notes";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_NOTE = "note";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_PRIORITY = "priority";
	
	private int id;
	private String title;
	private String note;
	private String timestamp;
	
	
	public static final String CREATE_TABLE =
			"CREATE TABLE " + TABLE_NAME + "("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ COLUMN_TITLE + " TEXT,"
					+ COLUMN_NOTE + " TEXT,"
					+ COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
					+ ")";
	
	public Note( ) {
	}
	
	public Note( int id , String title , String note , String timestamp ) {
		this.id = id;
		this.title = title;
		this.note = note;
		this.timestamp = timestamp;
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
}
