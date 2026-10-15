package com.soohyeon.booklog.web;

import com.soohyeon.booklog.domain.ReadingLog;
import com.soohyeon.booklog.service.ReadingLogService;
import com.soohyeon.booklog.web.argumentresolver.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController {

    private final ReadingLogService readingLogService;

    @GetMapping
    public String history(@LoginMember Long memberId,
                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
                          @RequestParam(defaultValue = "0") int page,
                          Model model) {

        Page<ReadingLog> result = readingLogService.findHistory(memberId, month, page);

        // 화면에서 "2026-10" 같은 월 제목으로 묶어 보여주기 위한 그룹핑 (정렬 순서 유지)
        Map<String, List<ReadingLog>> grouped = result.getContent().stream()
                .collect(Collectors.groupingBy(
                        l -> YearMonth.from(l.getReadingDate()).toString(),
                        LinkedHashMap::new, Collectors.toList()));

        model.addAttribute("page", result);
        model.addAttribute("grouped", grouped);
        model.addAttribute("month", month);
        return "history/history";
    }

    @PostMapping("/{logId}/delete")
    public String delete(@LoginMember Long memberId,
                         @PathVariable Long logId,
                         @RequestParam(required = false) String month,
                         @RequestParam(defaultValue = "0") int page,
                         RedirectAttributes redirectAttributes) {

        readingLogService.deleteLog(logId, memberId);
        redirectAttributes.addFlashAttribute("message", "독서 기록이 삭제되었습니다.");
        if (month != null && !month.isBlank()) {
            redirectAttributes.addAttribute("month", month);   // 보던 필터/페이지 유지
        }
        redirectAttributes.addAttribute("page", page);
        return "redirect:/history";
    }
}