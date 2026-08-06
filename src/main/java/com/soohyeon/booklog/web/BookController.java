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

    /**
     * 1. 도서 전체 목록 조회 및 필터링
     * v1.2 대시보드 반영
     * v1.3 검색 기능 추가
     * 
     */
    @GetMapping
    public String books(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BookStatus status,
            Model model)  {

        List<Book> books = bookService.searchBooks(keyword, status);
        List<Book> allBooks = bookService.findBooks();

        long totalCount = allBooks.size();
        long wishCount = allBooks.stream().filter(b -> b.getStatus() == BookStatus.WISH).count();
        long readingCount = allBooks.stream().filter(b -> b.getStatus() == BookStatus.READING).count();
        long doneCount = allBooks.stream().filter(b -> b.getStatus() == BookStatus.DONE).count();

        model.addAttribute("books", books);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword); // 추가: View에서 검색창 input에 입력값 유지용, status와 keyword를 교집합 검색

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("wishCount", wishCount);
        model.addAttribute("readingCount", readingCount);
        model.addAttribute("doneCount", doneCount);

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
        return bookOptional.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 도서 ID입니다: " + bookId));
    }

}
