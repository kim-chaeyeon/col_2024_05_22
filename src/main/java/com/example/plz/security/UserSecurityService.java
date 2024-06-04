package com.example.plz.security;

import com.example.plz.domain.member.entity.Member;
import com.example.plz.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> _User = this.memberRepository.findByUsername(username);

        if (_User.isEmpty()) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }

        Member member = _User.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}