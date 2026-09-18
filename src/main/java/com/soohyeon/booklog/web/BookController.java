package com.soohyeon.booklog.web;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;
import com.soohyeon.booklog.service.BookService;
import com.soohyeon.booklog.web.argumentresolver.LoginMember;
import com.soohyeon.booklog.web.form.BookForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    /**
     * 1. 도서 전체 목록 조회 및 필터링
     * v1.2 대시보드 반영
     * v1.3 검색 기능 추가
     */
    @GetMapping
    public String books(
            @LoginMember Long memberId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BookStatus status,
            @RequestParam(required = false, defaultValue = "id_desc") String sort,
            Model model) {

        List<Book> books = bookService.searchBooks(memberId, status, keyword, sort);
        List<Book> allBooks = bookService.findBooks(memberId);

        long totalCount = allBooks.size();
        long wishCount = allBooks.stream().filter(b -> b.getStatus() == BookStatus.WISH).count();
        long readingCount = allBooks.stream().filter(b -> b.getStatus() == BookStatus.READING).count();
        long doneCount = allBooks.stream().filter(b -> b.getStatus() == BookStatus.DONE).count();

        model.addAttribute("books", books);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword); // 추가: View에서 검색창 input에 입력값 유지용, status와 keyword를 교집합 검색
        model.addAttribute("sort", sort);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("wishCount", wishCount);
        model.addAttribute("readingCount", readingCount);
        model.addAttribute("doneCount", doneCount);

        return "books/books";
    }

    // 2-1
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("book", new BookForm());
        return "books/addForm";
    }

    /**
     * v1.4 add, edit 검증 기능 추가
     */
    @PostMapping("/add")
    public String add(@LoginMember Long memberId,
                      @Validated @ModelAttribute("book") BookForm form,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "books/addForm";
        }

        Book book = new Book();
        book.setTitle(form.getTitle());
        book.setAuthor(form.getAuthor());
        book.setStatus(form.getStatus());
        book.setRating(form.getRating());
        book.setSummary(form.getSummary());
        book.setMemo(form.getMemo());

        // 성공 로직
        Book savedBook = bookService.saveBook(book, memberId);
        redirectAttributes.addAttribute("bookId", savedBook.getId());
        redirectAttributes.addFlashAttribute("message", "책이 성공적으로 등록되었습니다!");
        return "redirect:/books/{bookId}";
    }

    // 3
    @GetMapping("/{bookId}")
    public String book(@LoginMember Long memberId, @PathVariable Long bookId, Model model) {
        Book book = optionalToBook(bookId, memberId);
        model.addAttribute("book", book);

        return "/books/book";
    }

    // 4-1
    @GetMapping("/{bookId}/edit")
    public String editForm(@LoginMember Long memberId, @PathVariable Long bookId, Model model) {
        Book book = optionalToBook(bookId, memberId);   // memberId 추가

        BookForm form = new BookForm();
        form.setTitle(book.getTitle());
        form.setAuthor(book.getAuthor());
        form.setStatus(book.getStatus());
        form.setRating(book.getRating());
        form.setSummary(book.getSummary());
        form.setMemo(book.getMemo());

        model.addAttribute("book", form);
        model.addAttribute("bookId", bookId);

        return "books/editForm";
    }

    // 4-2
    @PostMapping("/{bookId}/edit")
    public String edit(@LoginMember Long memberId,
                       @PathVariable Long bookId,
                       @Validated @ModelAttribute("book") BookForm form,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute("bookId", bookId);
            return "books/editForm";
        }

        Book updateParam = new Book();
        updateParam.setTitle(form.getTitle());
        updateParam.setAuthor(form.getAuthor());
        updateParam.setStatus(form.getStatus());
        updateParam.setRating(form.getRating());
        updateParam.setSummary(form.getSummary());
        updateParam.setMemo(form.getMemo());

        bookService.updateBook(bookId,memberId, updateParam);   // memberId 추가

        redirectAttributes.addAttribute("bookId", bookId);
        redirectAttributes.addFlashAttribute("message", "정보가 정상적으로 수정되었습니다!");

        return "redirect:/books/{bookId}";
    }

    // 5
    @PostMapping("{bookId}/delete")
    public String delete(@LoginMember Long memberId, @PathVariable Long bookId) {

        bookService.removeBook(bookId, memberId);   // memberId 추가

        return "redirect:/books";
    }

    /**
     * 옵셔널을 검증하여 Book 객체로 변환하는 메서드
     */
    private Book optionalToBook(Long bookId, Long memberId) {
        Optional<Book> bookOptional = bookService.findByBookId(bookId, memberId);
        return bookOptional.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 도서 ID입니다: " + bookId));
    }

}
