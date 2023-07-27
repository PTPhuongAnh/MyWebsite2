package com.example.Anh123.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.Anh123.model.Book;
import com.example.Anh123.model.User;

import jakarta.servlet.http.HttpSession;



@Controller
@CrossOrigin
public class AdminController {
	private AdminDAO adminDAO = new AdminDAO();
	private UserDAO userDAO = new UserDAO();

	@GetMapping("/admin/home")
	public String getAdminHomePage(Model model) {
		model.addAttribute("books", adminDAO.getAllBooks());
		return "admin";
	}

	@GetMapping("/admin/login")
	public String getAdminLoginPage() {
		return "adminLogin";
	}

	@PostMapping("/admin/login")
	public String adminLogin(Model model, @RequestParam String name, @RequestParam String password,
			HttpSession session) {
		if (adminDAO.selectAdmin(name, password).getId() > 0) {
			session.setAttribute("admin", adminDAO.selectAdmin(name, password));
			return "redirect:/admin/books";
		} else {
			String error = "Your account or your password was incorrect!";
			model.addAttribute("error", error);
			return "adminLogin";
		}
	}

	@GetMapping("/admin/logout")
	public String adminLogout(HttpSession session) {
		session.removeAttribute("admin");
		return "redirect:/admin/home";
	}
	
	@GetMapping("/admin/statistical")
	public String getAdminStatistical(Model model,HttpSession session) {
		List<Book> bs=adminDAO.getAllBooks();
		List<User> us=userDAO.getAllUser();
		model.addAttribute("size",bs.size());
		model.addAttribute("size1",us.size());
		return "statistical";
	}
	@GetMapping("/admin/listuser")
	public String getAdminUser(Model model) {
		model.addAttribute("user", userDAO.getAllUser());
		return "listuser";
	}
	
	@GetMapping("/admin/books")
	public String getBooks(Model model, HttpSession session) throws IOException {
		Object o = session.getAttribute("admin");
		if (o == null) {
			return "redirect:/admin/home";
		}
		List<Book> books = adminDAO.getAllBooks();
		for(Book book:books){
            int sold = adminDAO.soldBook(book.getId());
            adminDAO.updateBookSold(sold,book.getId());
        }
		model.addAttribute("books", books);
		return "admin";
	}

	@GetMapping("/admin/book/{bookId}")
	public String getBook(Model model, @PathVariable String bookId, HttpSession session) {
		Object o = session.getAttribute("admin");
		if (o == null) {
			return "redirect:/admin/home";
		}

		model.addAttribute("bookId", bookId);
		Book book = new Book();
		if (Integer.valueOf(bookId) > 0) {
			book = adminDAO.getBook(Integer.parseInt(bookId), "", "");
		}
		model.addAttribute("book", book);
		return "book-detail";
	}

	@PostMapping("/admin/book/save/{bookId}")
	public String addBook(Model model, Book book, @PathVariable String bookId,
			@RequestParam("imageInput") MultipartFile multipartFile, HttpSession session) throws Exception {
		Object o = session.getAttribute("admin");
		if (o == null) {
			return "redirect:/admin/home";
		}
		if (!adminDAO.checkExistedBookWhenInsert(book)) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			book.setBookCover(fileName);
			adminDAO.insertBook(book);

			String dirName = "imgs/" + adminDAO.getBook(0, book.getTitle(), book.getAuthor()).getId();

			Path uploadPath = Paths.get(dirName);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			try (InputStream inputStream = multipartFile.getInputStream()) {
				Path filePath = uploadPath.resolve(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
				System.out.println(filePath.toString());
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "redirect:/admin/books";
		} else {
			String error = "The book was existed. Please add a new book!";
			model.addAttribute("error", error);
			return "book-detail";
		}
	}

	@PutMapping("/admin/book/save/{bookId}")
	public String updateBook(Model model, Book book, @PathVariable String bookId,
			@RequestParam("imageInput") MultipartFile multipartFile, HttpSession session) throws Exception {
		Object o = session.getAttribute("admin");
		if (o == null) {
			return "redirect:/admin/home";
		}
		if (!adminDAO.checkExistedBookWhenUpdate(book)) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			if (fileName.equals("")) {
				book.setBookCover(adminDAO.getBook(book.getId(), "", "").getBookCover());
			} else {
				book.setBookCover(fileName);
			}
			adminDAO.updateBook(book);

			String dirName = "imgs/" + adminDAO.getBook(0, book.getTitle(), book.getAuthor()).getId();

			Path uploadPath = Paths.get(dirName);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			try (InputStream inputStream = multipartFile.getInputStream()) {
				Path filePath = uploadPath.resolve(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
				System.out.println(filePath.toString());
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "redirect:/admin/books";
		} else {
			String error = "The book was existed. Please add a new book!";
			model.addAttribute("error", error);
			return "book-detail";
		}
	}

	@DeleteMapping("/admin/book/delete/{bookId}")
	public String deleteBook(Model model, @PathVariable String bookId, HttpSession session) {
		Object o = session.getAttribute("admin");
		if (o == null) {
			return "redirect:/admin/home";
		}
		if (!adminDAO.selectCart(Integer.parseInt(bookId))) {
			adminDAO.deleteBook(Integer.parseInt(bookId));
			return "redirect:/admin/books";
		} else {
			String error = "The book was in an order.";
			model.addAttribute("error", error);
			return "admin";
		}
	}


}
