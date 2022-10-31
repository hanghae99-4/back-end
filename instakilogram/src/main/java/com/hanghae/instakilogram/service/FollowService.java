package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.dto.request.FollowRequestDto;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.entity.Follow;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.repository.FollowRepository;
import com.hanghae.instakilogram.repository.MemberRepository;
import com.hanghae.instakilogram.util.Verification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final Verification verification;

    private final FollowRepository followRepository;

    private final MemberRepository memberRepository;

    public ResponseDto<?> updateFollow(String toMemberId, Member fromMember) {


        Member toMember = memberRepository.findById(toMemberId).orElse(null);
        if(toMember == null){
            ResponseDto.fail("존재하지 않는 아이디입니다.");
        }

        Optional<Follow> findFollowing = followRepository.findByFromMemberAndToMember_memberId(fromMember, toMemberId);

        if(findFollowing.isEmpty()){
            FollowRequestDto followRequestDto = new FollowRequestDto(fromMember, toMember);
            Follow follow = new Follow(followRequestDto);
            followRepository.save(follow);
            return ResponseDto.success(true);
        }else{
            followRepository.deleteById(findFollowing.get().getId());
            return ResponseDto.success(false);
        }
    }
}
