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
public class FeedsListResponseDto {

    private Long feedId;
    private String memberId;
    private String memberImage;
    private String feedImage;
    private String nickname;
    private String contents;
    private List<Comments> commentsList;
    private boolean heartByMe;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long heartNum;
}
