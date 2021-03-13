package com.example.consumingrest.DAO;

import java.io.IOException;
import java.util.List;

import com.example.consumingrest.entiti.Note;

public interface NoteDAO {

	public List<Note> getAllNote() throws IOException;
	
	public Note getNote(int id) throws IOException;
	
	public Note setNote(int id, Note note) throws IOException;
	
	public boolean update(int id, Note note) throws IOException;
	
	public boolean remove(int id) throws IOException;
	
	public List<Note> search(String str) throws IOException;
	
	public int lastId() throws IOException;
}
