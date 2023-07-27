package com.example.Anh123.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cart {
	private Order order;
	private Map<Book, Integer> books = new HashMap<>();

	public Cart() {
		// TODO Auto-generated constructor stub
	}

	public Cart(Order order, Map<Book, Integer> books) {
		super();
		this.order = order;
		this.books = books;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Map<Book, Integer> getBooks() {
		return books;
	}

	public void setBooks(Book book, int quantity) {
		this.books.put(book, quantity);
	}

	public int getTotalPrice() {
		int totalPrice = 0;
		Set<Book> keySet = this.books.keySet();
		for (Book x : keySet) {
			totalPrice += x.getPrice() * this.books.get(x);
		}
		return totalPrice;
	}
}
