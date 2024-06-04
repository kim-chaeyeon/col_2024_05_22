package com.example.plz.domain.member.service;

import com.example.plz.domain.member.dto.JoinRequest;
import com.example.plz.domain.member.dto.LoginRequest;
import com.example.plz.domain.member.entity.Member;
import com.example.plz.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkLoginIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public void join(JoinRequest joinRequest) {
        memberRepository.save(joinRequest.toEntity());
    }

//    public Member login(LoginRequest loginRequest) {
//        Optional<Member> optionalMember = memberRepository.findByLoginId(loginRequest.getLoginId());
//
//        if (optionalMember.isPresent()) {
//            Member findMember = optionalMember.get();
//            if (passwordEncoder.matches(loginRequest.getPassword(), findMember.getPassword())) {
//                return findMember;
//            }
//        }
//        return null;
//    }

    public Member getLoginMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Transactional
    public Member whenSocialLogin(String username, String nickname) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) {
            return opMember.get();
        }

        // 새로운 회원 저장
        return signupSocialUser(username, nickname);
    }

    @Transactional
    public Member signupSocialUser(String username, String nickname) {
        // 소셜 로그인한 회원 저장
        return signup(username, "", nickname, "", 0,"", "", "", "", "", "");
    }

    @Transactional
    public Member signup(String username, String phoneNumber, String nickname, String password, int age,
                         String email, String gender, String region, String favoriteFood, String mbti, String sns) {
        Member member = Member.builder()
                .username(username)
                .phoneNumber(phoneNumber)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .email(email)
                .gender(gender)
                .region(region)
                .favoriteFood(favoriteFood)
                .age(age)
                .sns(sns)
                .mbti(mbti)
                .build();

        return memberRepository.save(member);
    }

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member getCurrentMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
    }
}
