package com.adminportal.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.adminportal.domain.Book;
import com.adminportal.service.BookService;

@Controller
@RequestMapping("/book")
public class BookController {

	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addBook(Model model) {

		Book book = new Book();
		model.addAttribute("book", book);

		return "addBook";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {

		bookService.save(book);

		MultipartFile bookImage = book.getBookImage();

		try {
			byte[] bytes = bookImage.getBytes();
			String name = book.getId() + ".png";
			BufferedOutputStream steam = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/images/book/" + name)));
			steam.write(bytes);
			steam.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:bookList";
	}

	@RequestMapping("/booklist")
	public String bookList(Model model) {

		List<Book> bookList = bookService.findAllBooks();
		model.addAttribute("bookList", bookList);

		return "bookList";
	}

	@RequestMapping("/bookinfo")
	public String bookInfo(@RequestParam("id") Long id, Model model) {

		Book book = bookService.findOneById(id);
		model.addAttribute("book", book);

		return "bookInfo";
	}

	@RequestMapping("/editbook")
	public String updateBook(@RequestParam("id") Long id, Model model) {
		Book book = bookService.findOneById(id);
		model.addAttribute("book", book);

		return "editBook";
	}

	@RequestMapping(value = "/editbook", method = RequestMethod.POST)
	public String bookListPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
		bookService.save(book);

		MultipartFile bookImage = book.getBookImage();

		if (!bookImage.isEmpty()) {

			try {
				byte[] bytes = bookImage.getBytes();
				String name = book.getId() + ".png";

				Files.delete(Paths.get("src/main/resources/static/images/book/" + name));

				BufferedOutputStream steam = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/images/book/" + name)));
				steam.write(bytes);
				steam.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return "redirect:/book/bookinfo?id="+book.getId();
	}

}
