package com.hanghae.instakilogram.dto.response;

import com.hanghae.instakilogram.entity.Comments;
import com.hanghae.instakilogram.entity.Heart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponseDto {

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
    private int heartNum;
}
