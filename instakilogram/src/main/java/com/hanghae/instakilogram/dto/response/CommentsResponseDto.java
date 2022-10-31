package com.hanghae.instakilogram.dto.responseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class CommentsResponseDto {

    private String comments;

    private String memberId;

    private String memberImage;

    private LocalDateTime createdAt;

}
