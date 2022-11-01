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
import com.hanghae.instakilogram.util.S3Uploader;
import com.hanghae.instakilogram.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final MemberRepository memberRepository;
    private final EntityManager entityManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final Util util;
    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseDto<?> signUp(SignupRequestDto signupRequestDto) {
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
    public ResponseDto<?> checkId(String id) {

        if (memberRepository.existsByMemberId(id))
            return ResponseDto.fail("사용 중인 ID입니다.");

        return ResponseDto.success("사용 가능한 ID 입니다.");
    }
    public ResponseDto<?> checkNickname(String id) {
        if (memberRepository.existsByMemberId(id))
            return ResponseDto.fail("사용 중인 Nickname 입니다.");

        return ResponseDto.success("사용 가능한 Nickname 입니다.");
    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Member member = util.getMember(loginRequestDto.getMemberId());

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword()))
            throw new RuntimeException("패스워드가 일치하지 않습니다.");

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        response.setHeader(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());

        return ResponseDto.success("로그인 성공");
    }


    public ResponseDto<?> memberUpdate(MemberUpdateRequestDto memberUpdateRequestDto, Member member, String memberId) throws IOException {

        Member preMember = util.getMember(member.getMemberId());

        if (!preMember.getMemberId().equals(memberId)) {
            return ResponseDto.fail("본인만 수정할 수 있습니다.");
        }
        Member newMember = Member.builder()
                .memberId(preMember.getMemberId())
                .username(preMember.getUsername())
                .nickname(memberUpdateRequestDto.getNickname())
                .password(preMember.getPassword())
                .introduce(memberUpdateRequestDto.getIntroduce())
                .authority(preMember.getAuthority())
                .memberImage(
                        (memberUpdateRequestDto.getMemberImage().getOriginalFilename().equals(""))?
                                null:s3Uploader.uploadFiles(memberUpdateRequestDto.getMemberImage(), "member", member, "member"))
                .build();

        memberRepository.save(newMember);
        return ResponseDto.success("회원정보를 업데이트 하셨습니다.");
    }

}
