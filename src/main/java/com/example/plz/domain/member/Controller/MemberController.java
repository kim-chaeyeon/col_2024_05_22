package com.example.plz.domain.member.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/login")
    public String loginPage(){
        return "member/login";
    }

    @GetMapping("/join")
    public String joinPage(){
        return "member/join";
    }
    @GetMapping("/admin")
    public String adminPage(){
        return "member/admin";
    }
}
