package com.example.plz.domain.member.Controller;

import com.example.plz.domain.member.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "member/login";
    }

    @GetMapping("/join")
    public String joinPage() {
        return "member/join";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "member/admin";
    }

    public class LoginRequest {
        private String loginId;
        private String password;

        // Getters and setters
        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
