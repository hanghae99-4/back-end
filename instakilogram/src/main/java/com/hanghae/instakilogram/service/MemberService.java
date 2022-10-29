package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.dto.TokenDto;
import com.hanghae.instakilogram.dto.request.*;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.entity.Authority;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.entity.RefreshToken;
import com.hanghae.instakilogram.repository.MemberRepository;
import com.hanghae.instakilogram.repository.RefreshTokenRepository;
import com.hanghae.instakilogram.security.jwt.TokenProvider;
import com.hanghae.instakilogram.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final MemberRepository memberRepository;
    private final EntityManager entityManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> signUp(SignupRequestDto signupRequestDto) {
        if (memberRepository.existsByMemberId(signupRequestDto.getMemberId()))
            throw new IllegalStateException("중복된 아이디입니다.");
        Member member = Member.builder()
                .memberId(signupRequestDto.getMemberId())
                .username(signupRequestDto.getUsername())
                .nickname(signupRequestDto.getNickname())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();

        entityManager.persist(member);

        memberRepository.save(member);
        return ResponseDto.success("회원가입 되었습니다.");
    }
    public ResponseDto<?> checkId(CheckIdRequestDto checkIdRequestDto) {

        if (memberRepository.existsByMemberId(checkIdRequestDto.getMemberId()))
            return ResponseDto.fail("사용 중인 ID입니다.");

        return ResponseDto.success("사용 가능한 ID 입니다.");
    }
    public ResponseDto<?> checkNickname(CheckNicknameRequestDto checkNicknameRequestDto) {
        if (memberRepository.existsByMemberId(checkNicknameRequestDto.getNickname()))
            return ResponseDto.fail("사용 중인 Nickname 입니다.");

        return ResponseDto.success("사용 가능한 Nickname 입니다.");
    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        System.out.println(11111111);
        System.out.println(authenticationToken);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        System.out.println(22222222);
        Member member = memberRepository.findById(loginRequestDto.getMemberId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));

        System.out.println(33333333);
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword()))
            throw new RuntimeException("패스워드가 일치하지 않습니다.");

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        System.out.println(refreshToken + "1111111");
        response.setHeader(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());

        return ResponseDto.success("로그인 성공");
    }


}
