package com.soohyeon.booklog.web;

import com.soohyeon.booklog.service.StatsService;
import com.soohyeon.booklog.web.argumentresolver.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/stats")
    public String stats(@LoginMember Long memberId, Model model) {
        model.addAttribute("stats", statsService.getStats(memberId));
        return "stats/stats";
    }
}
