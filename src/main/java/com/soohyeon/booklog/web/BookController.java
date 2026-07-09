package com.soohyeon.booklog.web;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;
import com.soohyeon.booklog.service.BookService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 1
    @GetMapping
    public String books(Model model) {
        List<Book> books = bookService.findBooks();
        model.addAttribute("books", books);
        return "books/books";
    }

    // 2-1
    @GetMapping("/add")
    public String addForm() {
        return "books/addForm";
    }

    /**
     * 2-2
     * v1.1: 메세지 커스텀
     * 변수를 url에 직접 더해서 쓰는 경우,
     * 데이터에 한글, 공백, 특수문자가 사용된 경우 URL 인코딩이 깨지므로
     * 현재의 방법을 사용하는 편이 좋음
     */
    @PostMapping("/add")
    public String add(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        Book savedBook = bookService.saveBook(book);
        redirectAttributes.addAttribute("bookId", savedBook.getId());
        redirectAttributes.addFlashAttribute("message", "책이 성공적으로 등록되었습니다!");
        return "redirect:/books/{bookId}";
    }

    // 3
    @GetMapping("/{bookId}")
    public String book(@PathVariable Long bookId, Model model) {
        Book book = optionalToBook(bookId);
        model.addAttribute("book", book);

        return "/books/book";
    }

    // 4-1
    @GetMapping("/{bookId}/edit")
    public String editForm(@PathVariable Long bookId, Model model) {
        Book book = optionalToBook(bookId);
        model.addAttribute("book", book);

        return "books/editForm";
    }

    // 4-2
    // v1.1 메세지 커스텀
    @PostMapping("/{bookId}/edit")
    public String edit(@PathVariable Long bookId, @ModelAttribute Book updateParam, RedirectAttributes redirectAttributes) {
        bookService.updateBook(bookId, updateParam);
        redirectAttributes.addAttribute("bookId", bookId);

        redirectAttributes.addFlashAttribute("message", "정보가 정상적으로 수정되었습니다!");
        return "redirect:/books/{bookId}";
    }

    // 5
    @PostMapping("{bookId}/delete")
    public String delete(@PathVariable Long bookId) {

        bookService.removeBook(bookId);

        return "redirect:/books";
    }

    /**
     * 옵셔널을 검증하여 Book 객체로 변환하는 메서드
     */
    private Book optionalToBook(Long bookId) {
        Optional<Book> bookOptional = bookService.findByBookId(bookId);
        Book book = bookOptional.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 도서 ID입니다: " + bookId));
        return book;
    }


    @PostConstruct
    public void init() {
        bookService.saveBook(new Book("데미안", "헤르만 헤세", BookStatus.READING, 5,
                "새는 알에서 나오려고 투쟁한다.", "내면의 성장을 유도하는 최고의 책"));
        bookService.saveBook(new Book("스프링 MVC 1편", "김영한", BookStatus.DONE, 3,
                "스프링 웹 기술의 핵심을 배운다.", "HTTP 요청과 응답, MVC 구조를 완벽하게 이해함"));
    }
}
