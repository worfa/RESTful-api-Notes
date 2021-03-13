package com.example.consumingrest.service;

import java.io.IOException;
import java.util.List;

import com.example.consumingrest.entiti.Note;

public interface ServiceNotes {

	
	
	public List<Note> showAllNotes() throws IOException;
	
	public Note create(Note note) throws IOException;
	
	public Note findNote(int id) throws IOException;
	
	public boolean updateNote(int id, Note substitute) throws IOException;
	
	public boolean removeNote(int id) throws IOException;
	
	public List<Note> searchNotes(String query) throws IOException;
}
