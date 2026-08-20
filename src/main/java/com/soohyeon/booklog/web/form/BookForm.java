package com.soohyeon.booklog.web.form;

import com.soohyeon.booklog.domain.BookStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 50)
    private String author;

    @NotNull
    private BookStatus status;

    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 100)
    private String summary;

    @Size(max = 500)
    private String memo;

}
