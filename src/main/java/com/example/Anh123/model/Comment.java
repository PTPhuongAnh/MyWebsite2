package com.example.Anh123.model;

public class Comment {
	private int id, rating;
	private User user;
	private Book book;
	private String comment;

	public Comment() {
		// TODO Auto-generated constructor stub
	}

	public Comment(int id, User user, Book book, int rating, String comment) {
		super();
		this.id = id;
		this.rating = rating;
		this.user = user;
		this.book = book;
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
