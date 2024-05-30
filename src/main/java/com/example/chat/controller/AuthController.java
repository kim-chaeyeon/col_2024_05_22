package com.example.chat.controller;

import com.example.chat.domain.member.entity.Member;
import com.example.chat.domain.member.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/auth/check")
    public Map<String, Object> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            response.put("isAuthenticated", true);
            response.put("username", authentication.getName());
        } else {
            response.put("isAuthenticated", false);
        }
        return response;
    }

    @GetMapping("/auth/member")
    public Member getCurrentUser() {
        return memberService.getCurrentMember();
    }
}
