package com.soohyeon.booklog.stats;

import com.soohyeon.booklog.domain.BookStatus;

public record StatusCount(BookStatus status, Long count) {
}
