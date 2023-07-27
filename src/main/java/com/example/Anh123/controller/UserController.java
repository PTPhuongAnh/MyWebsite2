package com.example.Anh123.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.Anh123.model.Book;
import com.example.Anh123.model.Cart;
import com.example.Anh123.model.Order;
import com.example.Anh123.model.User;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;

@Controller
@CrossOrigin
public class UserController {

	private UserDAO userDAO = new UserDAO();
	private AdminDAO adminDAO = new AdminDAO();

	@GetMapping("/user/home")
	public String getUserHomePage(Model model) {
		model.addAttribute("books", userDAO.getAllBooks());
		return "user";
	}

	@GetMapping("/user/login")
	public String getUserLoginPage() {
		return "userlogin";
	}
	@GetMapping("/user/forgotpass")
	public String getUserForgotPass() {
		return "forgetpassword";
	}

	@PostMapping("/user/login")
	public String userLogin(Model model, @RequestParam String name, @RequestParam String password,
			HttpSession session) {
		User user = userDAO.selectExistedUser(name, password);
		if (user.getId() > 0) {
			session.setAttribute("user", user);
			Order order = userDAO.getLastOrder(user.getId());
			if (order.getId() == 0 || order.getConfirm()) {
				userDAO.addOrder(user.getId());
				order = userDAO.getLastOrder(user.getId());
			}
			Cart cart = userDAO.getCart(order.getId());
			session.setAttribute("cart", cart);
			return "redirect:/user/books";
		} else {
			String error = "Your account or your password was incorrect!";
			model.addAttribute("error", error);
			return "userLogin";
		}
	}

	@GetMapping("/user/logout")
	public String userLogout(HttpSession session) {
		session.removeAttribute("user");
		session.removeAttribute("cart");
		return "redirect:/user/home";
	}

	@GetMapping("/user/register")
	public String getRegisterPage() {
		return "register";
	}

	@PostMapping("/user/register")
	public String register(Model model, @RequestParam String name, @RequestParam String password,
			@RequestParam String email, HttpSession session) {
		if (userDAO.selectUserByIdOrName(0, name).getName() == null
				&& adminDAO.selectAdminByName(name).getName() == null) {
			userDAO.insertUser(name, password, email);
			session.setAttribute("user", userDAO.selectExistedUser(name, password));
			return "redirect:/user/login";
		} else {
			String error = "Your name existed!";
			model.addAttribute("error", error);
			return "register";
		}
	}
	
	@PostMapping("/user/forgotpass")
	public String forget(Model model, @RequestParam String name, @RequestParam String password,
			@RequestParam String re_password, HttpSession session) {
		if (password.equals(re_password)) {
			userDAO.updateUser(name,password);
			return "redirect:/user/login";
		} else {
			String error = "The password doesn't match or you don't have an account";
			model.addAttribute("error", error);
			return "forgetpassword";
		}
	}

	@GetMapping("/user/books")
	public String getAllBooks(Model model, HttpSession session) {
		Object o = session.getAttribute("user");
		if (o == null) {
			return "redirect:/user/home";
		}
		model.addAttribute("books", userDAO.getAllBooks());
		return "user";
	}

	@GetMapping("/user/book/{bookId}")
	public String getBook(Model model, HttpSession session, @PathVariable String bookId) {
		model.addAttribute("book", userDAO.getBook(Integer.parseInt(bookId)));
		model.addAttribute("comments", userDAO.getComments(Integer.parseInt(bookId)));
		return "user-book-detail";
	}

	@PostMapping("/user/book/{bookId}/comment/post")
	public String insertComment(Model model, HttpSession session, @PathVariable String bookId,
			@RequestParam String userId, @RequestParam String ratingPost, @RequestParam String comment) {
		Object o = session.getAttribute("user");
		if (o == null) {
			return "redirect:/user/home";
		}
		userDAO.insertComment(Integer.parseInt(userId), Integer.parseInt(bookId), Integer.parseInt(ratingPost),
				comment);
		return "redirect:/user/book/" + bookId;
	}

	@PutMapping("/user/book/{bookId}/comment/update")
	public String updateComment(Model model, HttpSession session, @PathVariable String bookId,
			@RequestParam String commentId, @RequestParam String ratingPut, @RequestParam String comment) {
		Object o = session.getAttribute("user");
		if (o == null) {
			return "redirect:/user/home";
		}
		userDAO.updateComment(Integer.parseInt(commentId), Integer.parseInt(ratingPut), comment);
		return "redirect:/user/book/" + bookId;
	}

	@DeleteMapping("/user/book/{bookId}/comment/delete")
	public String deleteComment(Model model, HttpSession session, @PathVariable String bookId,
			@RequestParam String commentId) {
		Object o = session.getAttribute("user");
		if (o == null) {
			return "redirect:/user/home";
		}
		userDAO.deleteComment(Integer.parseInt(commentId));
		return "redirect:/user/book/" + bookId;
	}

	@GetMapping("/user/cart")
	public String getCart(HttpSession session) {
		Object o = session.getAttribute("user");
		if (o == null) {
			return "redirect:/user/home";
		}
		return "cart";
	}

	@PostMapping("/user/cart")
	public String addCart(HttpSession session, Model model, @SessionAttribute("user") User user,
			@SessionAttribute("cart") Cart cart, @RequestParam String bookId, @RequestParam String quantity) {
		Object o = session.getAttribute("user");
		if (o == null) {
			return "redirect:/user/home";
		}
		
		int userId = user.getId();
		if(cart.getOrder().getConfirm()) {
			
			userDAO.addOrder(userId);
			cart = userDAO.getCart(userDAO.getLastOrder(userId).getId());
		}

		Map<Book, Integer> books = cart.getBooks();
		boolean existed = false;
		int oldQuantity = 0;
		Set<Book> keySet = books.keySet();
		for (Book x : keySet) {
			if (x.getId() == Integer.parseInt(bookId)) {
				existed = true;
				oldQuantity = books.get(x);
				break;
			}
		}
		if (!existed) {
			userDAO.insertCart(cart.getOrder().getId(), Integer.parseInt(bookId), Integer.parseInt(quantity));
		} else {
			userDAO.updateCart(cart.getOrder().getId(), Integer.parseInt(bookId),
					Integer.parseInt(quantity) + oldQuantity);
		}

		Cart newCart = userDAO.getCart(cart.getOrder().getId());
		session.setAttribute("cart", newCart);

		return "redirect:/user/cart";
	}

	@DeleteMapping("/user/cart/delete")
	public String deleteItem(HttpSession session, @RequestParam String orderId, @RequestParam String bookId) {
		Object o = session.getAttribute("user");
		if (o == null) {
			return "redirect:/user/home";
		}
		userDAO.deleteItem(Integer.parseInt(orderId), Integer.parseInt(bookId));
		session.setAttribute("cart", userDAO.getCart(Integer.parseInt(orderId)));

		return "redirect:/user/cart";
	}
	
	@PostMapping("/user/order")
	public String addOrder(HttpSession session, @SessionAttribute("user") User user, @SessionAttribute("cart") Cart cart) {
		if(session.getAttribute("user") == null) {
			return "redirect:/user/home";
		}
		userDAO.updateOrderConfirm(cart.getOrder().getId(), 1);
		userDAO.updateOrderTotalPrice(cart.getOrder().getId(), cart.getTotalPrice());
		userDAO.updateOrderDate(cart.getOrder().getId(), new Date(System.currentTimeMillis()));
		session.setAttribute("cart", new Cart());
		return "redirect:/user/order";
	}
	
	@GetMapping("/user/order")
	public String getAllOrders(HttpSession session, Model model, @SessionAttribute("user") User user) {
		Object o = session.getAttribute("user");
		if(o == null) {
			return "redirect:/user/home";
		}
		List<Order> orders = userDAO.getConfirmedOrder(user.getId());
		model.addAttribute("orders", orders);
		return "order";
	}
	
	@GetMapping("/user/books/search")
	public String searchBooks(Model model, @PathParam("q") String  q) {
		if(q.isBlank()) {
			return "redirect:/user/home";
		}
		List<Book> books = userDAO.searchBooks(q, q, q);
		model.addAttribute("books", books);
		return "user";
	}
}
