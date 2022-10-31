package com.hanghae.instakilogram.dto.response;

import com.hanghae.instakilogram.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FeedsResponseDto {

    private Long feedId;

    private String memberId;

    private String nickname;

    private String memberImage;

    private String introduce;

    private String username;

    private String contents;

    private boolean heartByMe;

    private LocalDateTime createdAt;

    private Long heartNum;

    private List<CommentsResponseDto> commentsList;

}
