package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Author;
import com.soohyeon.booklog.domain.Tag;
import com.soohyeon.booklog.repository.AuthorRepository;
import com.soohyeon.booklog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Set<Author> resolveAuthors(String authorNames) 를 별도의 메서드로 분리
 * author와 tag를 같은 패턴으로 변환하므로
 */
@Component
@RequiredArgsConstructor
@Transactional
public class BookRelationResolver {

    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;

    public Set<Author> resolveAuthors(String names) {
        return split(names).stream()
                .map(name -> authorRepository.findByName(name)
                        .orElseGet(() -> authorRepository.save(new Author(name))))
                .collect(Collectors.toSet());
    }

    public Set<Tag> resolveTags(String names) {
        return split(names).stream()
                .map(name -> name.startsWith("#") ? name.substring(1).trim() : name)
                .filter(name -> !name.isBlank() && name.length() <= 30)
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .collect(Collectors.toSet());
    }

    private Set<String> split(String names) {
        if (names == null || names.isBlank()) return Set.of();
        return Arrays.stream(names.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());
    }
}
