package com.example.consumingrest.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.consumingrest.entiti.Note;
import com.example.consumingrest.service.ServiceNotes;


@RestController
public class GreetingController {

	@Autowired
	ServiceNotes serviceNotes;
	
	@GetMapping("/notes")
	public ResponseEntity<List<Note>> searchAll(@RequestParam(value="query", defaultValue = "") String query) throws IOException {
		
		if(query.isEmpty()) {
			List<Note> listNotes = serviceNotes.showAllNotes();
			if(listNotes.size() == 0) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
			return new ResponseEntity<>(listNotes, HttpStatus.OK);}
		else {
			List<Note> listNotes = serviceNotes.searchNotes(query);
			if(listNotes.size() == 0)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(listNotes, HttpStatus.OK);
		}	
	}
	
	@PostMapping("/notes")
	public ResponseEntity<Note> create(@RequestBody Note note) throws IOException {
		if(note.getContent() == null){
			 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else 
			 note = serviceNotes.create(note);
			 return new ResponseEntity<>(note, HttpStatus.OK);
	}

	@GetMapping("/notes/{id}")
	public ResponseEntity<Note> searchId(@PathVariable("id") int id) throws IOException {
		Note note = serviceNotes.findNote(id);
		if(note == null || note.getContent() == null){
			 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else 
			 return new ResponseEntity<>(note, HttpStatus.OK);
		 			
	}
	
	@PutMapping("/notes/{id}")
	public ResponseEntity<Note> update(@PathVariable(name = "id") int id,@RequestBody Note note) throws IOException {
		boolean check = serviceNotes.updateNote(id, note);
		if(check)
			return new ResponseEntity<>(HttpStatus.OK);
		else 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/notes/{id}")
	public ResponseEntity<Note> remove(@PathVariable("id") int id) throws IOException{
		boolean check = serviceNotes.removeNote(id);
		if(check)
			return new ResponseEntity<>(HttpStatus.OK);
		else 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
