package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.entity.Follow;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.repository.FollowRepository;
import com.hanghae.instakilogram.repository.MemberRepository;
import com.hanghae.instakilogram.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final Util util;

    private final FollowRepository followRepository;

    private final MemberRepository memberRepository;

    public ResponseDto<?> updateFollow(String toMemberId, Member fromMember) {


        Member toMember = memberRepository.findById(toMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));

        Optional<Follow> findFollowing = followRepository.findByFromMemberAndToMember_memberId(fromMember, toMemberId);

        if(findFollowing.isEmpty()){
            Follow follow = Follow.builder()
                    .toMember(toMember)
                    .fromMember(fromMember)
                    .build();
            followRepository.save(follow);
            return ResponseDto.success(true);
        }else{
            followRepository.deleteById(findFollowing.get().getId());
            return ResponseDto.success(false);
        }
    }
}
