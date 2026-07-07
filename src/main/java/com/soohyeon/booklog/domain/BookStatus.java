package com.soohyeon.booklog.domain;

public enum BookStatus {
    WISH("읽고 싶은 책"),
    READING("읽는 중"),
    DONE("완독");

    private final String description;

    BookStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
