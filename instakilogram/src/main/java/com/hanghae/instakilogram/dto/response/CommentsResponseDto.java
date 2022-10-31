package com.hanghae.instakilogram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsResponseDto {
    private Long id;
    private String nickname;
    private String contents;
    private String memeberImage;
    private Long feedId;
    private String memberId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
