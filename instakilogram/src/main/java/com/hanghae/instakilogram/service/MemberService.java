package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.dto.TokenDto;
import com.hanghae.instakilogram.dto.request.*;
import com.hanghae.instakilogram.dto.response.MemberResponseDto;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.entity.Authority;
import com.hanghae.instakilogram.entity.Follow;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.entity.RefreshToken;
import com.hanghae.instakilogram.repository.FollowRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final FollowRepository followRepository;
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
        Follow follow = Follow.builder()
                .toMember(member)
                .fromMember(member)
                .build();
        followRepository.save(follow);

        memberRepository.save(member);
        return ResponseDto.success("???????????? ???????????????.");
    }
    public ResponseDto<?> checkId(String id) {

        if (memberRepository.existsByMemberId(id))
            return ResponseDto.fail("?????? ?????? ID?????????.");

        return ResponseDto.success("?????? ????????? ID ?????????.");
    }
    public ResponseDto<?> checkNickname(String id) {
        if (memberRepository.existsByMemberId(id))
            return ResponseDto.fail("?????? ?????? Nickname ?????????.");

        return ResponseDto.success("?????? ????????? Nickname ?????????.");
    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Member member = util.getMember(loginRequestDto.getMemberId());

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword()))
            throw new RuntimeException("??????????????? ???????????? ????????????.");

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        response.setHeader(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());

        return ResponseDto.success("????????? ??????");
    }


    public ResponseDto<?> memberUpdate(MemberUpdateRequestDto memberUpdateRequestDto, Member member, String memberId) throws IOException {

        Member preMember = util.getMember(member.getMemberId());

        if (!preMember.getMemberId().equals(memberId)) {
            return ResponseDto.fail("????????? ????????? ??? ????????????.");
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
        return ResponseDto.success("??????????????? ???????????? ???????????????.");
    }

    @Transactional
    public ResponseDto<?> search(String keyword) {
        List<Member> memberList = memberRepository.findByMemberIdContaining(keyword);
        List<String> memberIdList = memberList.stream().map(Member::getMemberId).collect(Collectors.toList());
        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .memberIdList(memberIdList)
                .build();
        return ResponseDto.success(memberResponseDto);
    }

}
