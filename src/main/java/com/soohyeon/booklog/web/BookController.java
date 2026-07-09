package com.soohyeon.booklog.web;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;
import com.soohyeon.booklog.service.BookService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public String books(Model model) {
        List<Book> books = bookService.findBooks();
        model.addAttribute("books", books);
        return "books/books";
    }

    /**
     * 새 책 등록 폼 화면 이동
     * URL: GET /books/add
     */
    @GetMapping("/add")
    public String addForm() {
        return "books/addForm";
    }

    /**
     * 새 책 등록
     * @ModelAttribute  사용 시, model.addAttribute() 자동 추가
     * PRG 패턴 적용
     * URL: POST /books/add
     */
    @PostMapping("/add")
    public String add(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        Book savedBook = bookService.saveBook(book);
        redirectAttributes.addAttribute("bookId", savedBook.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/books/{bookId}";
    }

    @PostConstruct
    public void init() {
        bookService.saveBook(new Book("데미안", "헤르만 헤세", BookStatus.READING, 5,
                "새는 알에서 나오려고 투쟁한다.", "내면의 성장을 유도하는 최고의 책"));
        bookService.saveBook(new Book("스프링 MVC 1편", "김영한", BookStatus.DONE, 3,
                "스프링 웹 기술의 핵심을 배운다.", "HTTP 요청과 응답, MVC 구조를 완벽하게 이해함"));
    }
}
