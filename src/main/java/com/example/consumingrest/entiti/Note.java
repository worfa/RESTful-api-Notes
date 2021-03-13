package com.example.consumingrest.entiti;

public class Note {

	private int id;
	private String title;
	private String content;
	
	public Note() {}
	
	public Note(String title) {
		this.title = title;
	}
	
	public Note(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
	public Note(int id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getId())
		.append(',')
		.append(getTitle())
		.append(',')
		.append(getContent());
		return stringBuilder.toString();
	}
}
