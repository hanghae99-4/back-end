package com.hanghae.instakilogram.dto.requestDto;

import com.hanghae.instakilogram.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowRequestDto {

    private Member Follwer;

    private Member Follwing;

}
