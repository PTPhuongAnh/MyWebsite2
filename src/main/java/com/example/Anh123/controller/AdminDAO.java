package com.example.Anh123.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.Anh123.model.Admin;
import com.example.Anh123.model.Book;

public class AdminDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/bookstore";
	private String jdbcUsername = "root";
	private String jdbcPassword = "1924052002";
	
	private static final String SELECT_ALL_BOOKS = "select * from book";
	private static final String SELECT_ADMIN = "select * from admin where adname=? and password=?";
	private static final String SELECT_BOOK_BY_ID="select *from book where id=? or(title=? and author=?)";
	private static final String DELETE_BOOK = "delete from book where id = ?";
	private static final String SELECT_CART = "select * from cart where bookId = ?";
	private static final String INSERT_BOOK = "insert into book(title, author, category, `release`, pages, price, sold, description, `bookcover`) values (?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_BOOK = "update book set title = ?, author = ?, category = ?, `release` = ?, pages = ?, price = ?, description = ?, bookcover = ? where id = ?";
	
	private static final String UPDATE_BOOK_SOLD = "update book set sold = ? where id = ?";
	private static final String SELECT_EXISTED_BOOK_WHEN_INSERT = "select * from book where title = ? and author = ?";
	private static final String SELECT_EXISTED_BOOK_WHEN_UPDATE = "select * from book where title = ? and author = ? and id != ?";
	private static final String SELECT_ADMIN_BY_NAME = "select * from admin where adname = ?";
	private static final String COUNT_BOOK = "SELECT COUNT(*) AS \"Tong\" FROM book\r\n";
	private static final String SELECT_SOLD="select * from bookstore.cart where bookId=?";
	
	public AdminDAO() {
		// TODO Auto-generated constructor stub
	}

	protected Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(this.jdbcURL, this.jdbcUsername, this.jdbcPassword);
		return connection;
	}
	public List<Book> getAllBooks() {
		List<Book> books = new ArrayList<>();
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(SELECT_ALL_BOOKS);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String author = rs.getString("author");
				String category = rs.getString("category");
				Date release = rs.getDate("release");
				int pages = rs.getInt("pages");
				int sold = rs.getInt("sold");
				String desc = rs.getString("description");
				String bookCover = rs.getString("bookcover");
				int price = rs.getInt("price");
				books.add(new Book(id, title, author, category, release, pages, sold, desc, bookCover, price));
			}
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return books;
	}
	public Admin selectAdminByName(String name) {
		Admin admin = new Admin();
		try {
			Connection connection = this.getConnection();
			PreparedStatement ps = connection.prepareStatement(SELECT_ADMIN_BY_NAME);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				admin.setId(rs.getInt("id"));
				admin.setName(rs.getString("adminname"));
				admin.setPassword(rs.getString("password"));
				admin.setEmail(rs.getString("email"));
			}
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return admin;
	}

	
	public Admin selectAdmin(String name, String password) {
		Admin admin = new Admin();
		try {
			Connection connection = this.getConnection();
			PreparedStatement ps = connection.prepareStatement(SELECT_ADMIN);
			ps.setString(1, name);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				admin.setId(rs.getInt("id"));
				admin.setName(rs.getString("adname"));
				admin.setPassword(rs.getString("password"));
				admin.setEmail(rs.getString("email"));
			}
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return admin;
	}
	
	public Book getBook(int id, String title, String author) {
		Book book = new Book();
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(SELECT_BOOK_BY_ID);
			ps.setInt(1, id);
			ps.setString(2, title);
			ps.setString(3, author);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				book.setId(rs.getInt("id"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setCategory(rs.getString("category"));
				book.setRelease(rs.getDate("release"));
				book.setPages(rs.getInt("pages"));
				book.setPrice(rs.getInt("price"));
				book.setSold(rs.getInt("sold"));
				book.setDescription(rs.getString("description"));
				book.setBookCover(rs.getString("bookcover"));
			}
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return book;
	}

	public void insertBook(Book book) {
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(INSERT_BOOK);
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor());
			ps.setString(3, book.getCategory());
			ps.setDate(4, book.getRelease());
			ps.setInt(5, book.getPages());
			ps.setInt(6, book.getPrice());
			ps.setInt(7, 0);
			ps.setString(8, book.getDescription());
			ps.setString(9, book.getBookCover());
			ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void updateBook(Book book) {
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(UPDATE_BOOK);
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor());
			ps.setString(3, book.getCategory());
			ps.setDate(4, book.getRelease());
			ps.setInt(5, book.getPages());
			ps.setInt(6, book.getPrice());
			ps.setString(7, book.getDescription());
			ps.setString(8, book.getBookCover());
			ps.setInt(9, book.getId());
			ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void updateBookSold(int sold,int id) {
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(UPDATE_BOOK_SOLD);
			ps.setInt(2, id);
			ps.setInt(1, sold);
			ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public boolean checkExistedBookWhenInsert(Book book) {
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(SELECT_EXISTED_BOOK_WHEN_INSERT);
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean checkExistedBookWhenUpdate(Book book) {
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(SELECT_EXISTED_BOOK_WHEN_UPDATE);
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor());
			ps.setInt(3, book.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public void deleteBook(int id) {
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_BOOK);
			ps.setInt(1, id);
			ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean selectCart(int bookId) {
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(SELECT_CART);
			ps.setInt(1, bookId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public int soldBook(int id) {
		int quantity=0;
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(SELECT_SOLD);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				quantity+=rs.getInt("quantity");
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return quantity;
	}
	public static void main(String args[]) {
		AdminDAO ad=new AdminDAO();
		System.out.println(ad.soldBook(3));
	}
	
}
