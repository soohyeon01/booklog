package com.soohyeon.booklog.web;

import com.soohyeon.booklog.service.ReadingLogService;
import com.soohyeon.booklog.web.argumentresolver.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books/{bookId}/logs")
public class ReadingLogController {

    private final ReadingLogService readingLogService;

    @PostMapping
    public String add(@LoginMember Long memberId,
                      @PathVariable Long bookId,
                      @RequestParam String content,
                      @RequestParam(required = false)
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate readingDate,
                      RedirectAttributes redirectAttributes) {

        // 문자열 앞 뒤 공백 제거
        String trimmed = content == null ? "" : content.trim();

        if (trimmed.isEmpty() || trimmed.length() > 1000) {
            redirectAttributes.addFlashAttribute("logError", "기록은 1자 이상 1000자 이하로 입력해주세요.");
        } else if (readingDate != null && readingDate.isAfter(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("logError", "읽은 날짜는 오늘 이후로 설정할 수 없습니다.");
        } else {
            readingLogService.addLog(bookId, memberId, trimmed, readingDate);
            redirectAttributes.addFlashAttribute("message", "독서 기록이 추가되었습니다.");
        }
        redirectAttributes.addAttribute("bookId", bookId);
        return "redirect:/books/{bookId}";
    }

    @PostMapping("/{logId}/delete")
    public String delete(@LoginMember Long memberId,
                         @PathVariable Long bookId,
                         @PathVariable Long logId,
                         RedirectAttributes redirectAttributes) {

        readingLogService.deleteLog(logId, memberId);
        redirectAttributes.addFlashAttribute("message", "독서 기록이 삭제되었습니다.");
        redirectAttributes.addAttribute("bookId", bookId);
        return "redirect:/books/{bookId}";
    }

}
