package com.example.consumingrest.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.consumingrest.DAO.NoteDAOImpl;
import com.example.consumingrest.entiti.Note;

@Service
public class ServiceNotesImpl implements ServiceNotes {

	@Autowired
	NoteDAOImpl noteDAOImpl;
	private AtomicInteger atomicInt = new AtomicInteger();
	
	@Override
	public List<Note> showAllNotes() throws IOException {
		List<Note> noteList = noteDAOImpl.getAllNote();
		return noteList;
	}

	@Override
	public Note create(Note note) throws IOException {
		atomicInt.set(noteDAOImpl.lastId());
		if(atomicInt.get() < 0) {
			atomicInt.set(0);
			noteDAOImpl.setNote(atomicInt.get(), note);
		}
		else
			note = noteDAOImpl.setNote(atomicInt.incrementAndGet(), note);
		return note;
	}

	@Override
	public Note findNote(int id) throws IOException{
		return noteDAOImpl.getNote(id);
	}
	
	@Override
	public boolean updateNote(int id, Note note) throws IOException {
		note.setId(id);
		boolean check = noteDAOImpl.update(id, note);
		return check;
	}

	@Override
	public boolean removeNote(int id) throws IOException {

		boolean check = noteDAOImpl.remove(id);
		return check;
	}

	@Override
	public List<Note> searchNotes(String query) throws IOException{

		List<Note> noteList = noteDAOImpl.search(query);
		
		return noteList;
	}
	
	
}
