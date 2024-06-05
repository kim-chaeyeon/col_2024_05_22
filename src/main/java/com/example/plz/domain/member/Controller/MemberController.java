package com.example.plz.domain.member.Controller;

import com.example.plz.domain.email.EmailService;
import com.example.plz.domain.member.dto.LoginRequest;
import com.example.plz.domain.member.entity.Member;
import com.example.plz.domain.member.service.MemberService;
import com.example.plz.domain.member.service.VerificationCodeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final EmailService emailService;
    private final MemberService memberService;
    private final VerificationCodeService verificationCodeService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("/myPage")
    public String myPage(Model model) {
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        return "member/myPage";
    }

    @GetMapping("/modify")
    public String modifyForm(Model model){
        return "member/modify";
    }

    @PostMapping("/modify")
    public String modify(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("nickname") String nickname,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("age") int age,
            @RequestParam("region") String region,
            @RequestParam("mbti") String mbti,
            @RequestParam("sns") String sns,
            @RequestParam("favoriteFood") String favoriteFood,
            @RequestParam("thumbnail") MultipartFile thumbnail
    ) {
        // 클라이언트에서 요청 시 'username' 매개변수 제거

        memberService.modify(phoneNumber, nickname, password, age, email, region, favoriteFood, mbti, sns);

        // 회원 정보 수정 후 리다이렉트
        return "redirect:/member/myPage";
    }


    @GetMapping("/admin")
    public String adminPage() {
        return "member/admin";
    }

    @GetMapping("/signup")
    public String signForm(Model model) {
        return "member/signup"; // signup.html을 반환
    }


    @PostMapping("/signup")
    public String signup(@RequestParam("username") String username,
                         @RequestParam("phoneNumber") String phoneNumber,
                         @RequestParam("nickname") String nickname,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email,
                         @RequestParam("age") int age,
                         @RequestParam("gender") String gender,
                         @RequestParam("region") String region,
                         @RequestParam("mbti") String mbti,
                         @RequestParam("sns") String sns,
                         @RequestParam("favoriteFood") String favoriteFood,
                         @RequestParam("thumbnail") MultipartFile thumbnail
    ) {

        // 이메일 확인용 코드 생성
        String verificationCode = verificationCodeService.generateVerificationCode(email);
        // 회원가입 확인 이메일 보내기

        String subject = "회원가입 인증코드";
        String body = "회원가입인증 코드입니다. : " + verificationCode;
        emailService.send(email, subject, body);

        // 파일 업로드 성공 시 회원 가입 처리
        memberService.signup(username, phoneNumber, nickname, password, age, email, gender, region, favoriteFood, mbti, sns);

        // 회원 가입 후 로그인 페이지로 리다이렉트
        return "redirect:/member/verifyCode";
    }
    @GetMapping("/verifyCode")
    public String verifyCodeForm(Model model) {
        return "member/verifyCode"; // verifyCode.html을 반환
    }
    @PostMapping("/verifyCode")
    public String verifyCode(@RequestParam("verification") String verificationCode, HttpSession session) {
        String storedVerification = (String) session.getAttribute("verificationCode"); // 올바른 세션 키 사용
        if (verificationCode != null && verificationCode.equals(storedVerification)) {
            session.removeAttribute("verificationCode"); // 세션에서 올바른 키 제거
            return "member/login";
        } else {
            return "member/verifyCode";
        }
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
