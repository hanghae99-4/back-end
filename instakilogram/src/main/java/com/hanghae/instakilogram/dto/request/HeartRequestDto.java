package com.hanghae.instakilogram.dto.request;

import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HeartRequestDto {

    private Member member;

    private Feeds feeds;
}
