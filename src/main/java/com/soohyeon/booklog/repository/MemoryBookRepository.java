package com.soohyeon.booklog.repository;

import com.soohyeon.booklog.domain.Book;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryBookRepository implements BookRepository {

    // 동시성 문제를 고려하여 실제 프로젝트에서는 ConcurrentHashMap 사용
    private static final Map<Long, Book> store = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Book save(Book book) {
        book.setId(sequence.incrementAndGet());
        store.put(book.getId(), book);
        return book;
    }

    // 찾는 데이터가 없을 경우를 대비해 Optional로 감싸기
    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    // v1: 최신 등록 순으로 리스트 조회하기
    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>(store.values());
        Collections.reverse(books);
        return books;
    }

    @Override
    public void update(Long bookId, Book updateParam) {
        Book findBook = store.get(bookId);
        if (findBook != null) {
            findBook.setTitle(updateParam.getTitle());
            findBook.setAuthor(updateParam.getAuthor());
            findBook.setStatus(updateParam.getStatus());
            findBook.setRating(updateParam.getRating());
            findBook.setSummary(updateParam.getSummary());
            findBook.setMemo(updateParam.getMemo());
        }
    }

    @Override
    public void delete(Long bookId) {
        store.remove(bookId);
    }

    // 테스트나 초기 데이터 세팅 시 저장소 초기화를 위한 편의 메서드
    public void clearStore() {
        store.clear();
    }

}
