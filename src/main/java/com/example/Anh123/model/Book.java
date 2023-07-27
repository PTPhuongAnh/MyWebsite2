package com.example.Anh123.model;

import java.sql.Date;

public class Book {
	private int id, pages, sold;
	private String title, author, category, bookCover, description;
	private Date release;
	private int price;

	public Book() {
		// TODO Auto-generated constructor stub
	}

	public Book(int id, String title, String author, String category, Date release, int pages, int sold,
			String description, String bookCover, int price) {
		super();
		this.id = id;
		this.pages = pages;
		this.sold = sold;
		this.title = title;
		this.author = author;
		this.category = category;
		this.release = release;
		this.description = description;
		this.bookCover = bookCover;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getRelease() {
		return release;
	}

	public void setRelease(Date release) {
		this.release = release;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getBookCover() {
		return bookCover;
	}

	public String getBookCoverPath() {
		return "/imgs/" + this.id + "/" + this.bookCover;
	}

	public void setBookCover(String bookCover) {
		this.bookCover = bookCover;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	

}
