//package com.soohyeon.booklog.init;
//
//import com.soohyeon.booklog.domain.Member;
//import com.soohyeon.booklog.repository.MemberRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.regex.Pattern;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class LegacyPasswordMigration implements CommandLineRunner {
//
//    // BCrypt 해시 형식: $2a$10$ + 53자
//    private static final Pattern BCRYPT = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$.{53}$");
//
//    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//        int migrated = 0;
//        for (Member member : memberRepository.findAll()) {
//            if (!BCRYPT.matcher(member.getPassword()).matches()) {
//                member.setPassword(passwordEncoder.encode(member.getPassword()));   // 변경 감지로 UPDATE
//                migrated++;
//            }
//        }
//        log.info("[마이그레이션] 평문 비밀번호 {}건을 BCrypt 해시로 변환했습니다.", migrated);
//    }
//}