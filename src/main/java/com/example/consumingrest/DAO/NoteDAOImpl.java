package com.example.consumingrest.DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.consumingrest.entiti.Note;

@Service
public class NoteDAOImpl implements NoteDAO {
	
	public List<Note> listNotes = new ArrayList<Note>();
	
	@Value("${countTitle}")
	private int countNameTitle;
	private final String pathFileCSV = "test.csv";	
	private Boolean checkFullReadFile = false;

	private String readEndLine() throws IOException {
		
		String line = null;
		long startIndx;
	
		RandomAccessFile reader = new RandomAccessFile(pathFileCSV, "r");
		StringBuilder strBuilder = new StringBuilder();
		
		startIndx = reader.length() - 1;
		if(startIndx >= 0)
		reader.seek(startIndx);

		boolean check = false;
		int c = -1;
		if(startIndx > 0) {
		while (!check) {
            switch (c = reader.read()) {
            case -1:
            case '\n':
                check = true;
                break;
            case '\r':
                check = true;
                long cur = reader.getFilePointer();
                if ((reader.read()) != '\n') {
                    reader.seek(cur);
                }
                break;
            default:
                strBuilder.append((char)c);
                startIndx--;
                break;
            }
            if(startIndx >= 0)
            reader.seek(startIndx);
            else 
            	break;
        }

		reader.close();
		line = strBuilder.reverse().substring(0);
		}
		return line;
	}

	private List<Note> readFile() {
		
		String line = null;
		Scanner scanner = null;
		int index = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pathFileCSV));
			
			try {
				while((line = reader.readLine()) != null) {
					scanner = new Scanner(line);
					scanner.useDelimiter(",");
					Note note = new Note();
					while (scanner.hasNext()) {
						String data = scanner.next();
						if (index == 0)
							note.setId(Integer.parseInt(data));
						else if (index == 1)
								if(data.isBlank() || data.equals("null"))
									note.setTitle(null);
								else
									note.setTitle(data);
						else if (index == 2) {
							note.setContent(data);
						}
						index++;
					}
					index = 0;
					this.listNotes.add(note);
				}
				reader.close();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return listNotes;
	}

	private void setTitleNote(Note note){
			if(note.getTitle() == null) {
				String nameTitle = null;
				nameTitle = note.getContent().substring(0, countNameTitle);
				note.setTitle(nameTitle);
			}
	}
	
	private void updateFile() throws IOException {
		//создаем первую строку с удалением всей прошлой информации
		BufferedWriter writer = new BufferedWriter(new FileWriter(pathFileCSV));

		writer.write(this.listNotes.get(0).toString());
		writer.newLine();
		writer.close();
				
		//даем возможность не удалять старую информацию и записываем новую
		writer = new BufferedWriter(new FileWriter(pathFileCSV,true));
		int i = 1;
		while(i <= this.listNotes.size() - 1) {
			writer.write(this.listNotes.get(i).toString());
			if(i != this.listNotes.size() - 1)
				writer.newLine();
			i++;
		}
				
		writer.close();
	}
	
	private Note binarySearch(int id) {
		
		Note note = new Note();
		
		int pointerFirst = 0;
		int pointerLast = listNotes.size() - 1;
		int pointerMid = (pointerLast + pointerFirst) / 2;
		boolean check = false;

		while(pointerFirst <= pointerLast) {
			pointerMid = (pointerFirst + pointerLast) / 2;
			if(listNotes.get(pointerMid).getId() < id) {
				pointerFirst = pointerMid + 1;
			} else 
				if(listNotes.get(pointerMid).getId() > id) {
					pointerLast = pointerMid - 1;	
			} else 
				if(listNotes.get(pointerMid).getId() == id) {
					note = listNotes.get(pointerMid);
					check = true;
					break;
			}
		}
		
		if(check) {
			return note;
		} else
			return null;
	}
	
	@Override
	public List<Note> getAllNote() {
		
		this.listNotes.removeAll(listNotes);
		this.listNotes = readFile();
		for(Note note : listNotes)
		setTitleNote(note);
		this.checkFullReadFile = true;
		
		return listNotes;
	}

	@Override
	public Note getNote(int id) throws IOException {
		
		Note note = new Note();
		
		if(!checkFullReadFile)
			getAllNote();
		
		note = binarySearch(id);
		
		if(note != null) {
		setTitleNote(note);
		return note;
		} else
			return null;
	}

	@Override
	public Note setNote(int id, Note note) throws IOException {
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(pathFileCSV,true));
		StringBuilder stringBuilder = new StringBuilder();
		//составляем строку
		if(note.getTitle() == null || note.getTitle().isBlank()) {
			stringBuilder.append(id)
				.append(',')
				.append(',')
				.append(note.getContent());
			}
		else { 
			stringBuilder.append(id)
				.append(',')
				.append(note.getTitle())
				.append(',')
				.append(note.getContent());
		}
		//записываем строку в начало/конец файла
		if(id == 0){
			writer.write(stringBuilder.toString());
			note.setId(id);
			listNotes.add(note);
		} 
		else {
			writer.newLine();
			writer.write(stringBuilder.toString());
			note.setId(id);
			listNotes.add(note);
		}
		writer.close();
		
		return listNotes.get(listNotes.size() - 1);
	}

	@Override
	public int lastId() throws IOException {
		
		int id = -1;
		String line = null;
		Scanner scanner;
		
		// получаем последнюю строку
		line = readEndLine();
		if(line != null) {
		// поиск ид последней строки
			scanner = new Scanner(line);
			scanner.useDelimiter(",");
			String data = scanner.next();
			id = Integer.parseInt(data);
			scanner.close();
		}
		
		return id;
	}

	@Override
	public boolean update(int id, Note note) throws IOException {
		
		boolean check = false;
		
		//проверяем на данные, загружены они в память или нет
		if(!this.checkFullReadFile) 
			getAllNote();
		
		//поиск интересующей записи
		Note noteUp;
		if((noteUp = binarySearch(id)) != null)
			check = true;
		if(check) {
			this.listNotes.set(this.listNotes.indexOf(noteUp), note);
			
			//обновляем документ с поступившими обновлениями
			updateFile();
			
			return check;
		} else
			return check;
	}

	@Override
	public boolean remove(int id) throws IOException {
		
		boolean check = false;
		
		//проверяем на данные в памяти
		if(!this.checkFullReadFile) 
			getAllNote();
	
		check = this.listNotes.remove(binarySearch(id));
		//обновляем документ с поступившими обновлениями
		updateFile();
		
		return check;
	}

	@Override
	public List<Note> search(String str) throws IOException {
		
		List<Note> selectionListNotes = new ArrayList<>();
		readFile();
		
		return selectionListNotes;
	}
}
