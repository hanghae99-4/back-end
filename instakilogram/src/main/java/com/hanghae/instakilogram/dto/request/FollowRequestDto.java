package com.hanghae.instakilogram.dto.request;


import com.hanghae.instakilogram.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowRequestDto {

    private Member fromMember;

    private Member toMember;

}
