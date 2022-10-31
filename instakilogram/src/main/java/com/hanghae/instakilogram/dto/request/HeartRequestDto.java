package com.hanghae.instakilogram.dto.requestDto;

import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@AllArgsConstructor
public class HeartRequestDto {

    private Member member;

    private Feeds feeds;

}
