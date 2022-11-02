package com.hanghae.instakilogram.dto.response;

import com.hanghae.instakilogram.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedsListResponseDto {
    private String memberId;
    private String username;
    private String memberImage;
    private String introduce;
    private String nickname;
    private List<FeedResponseDto> feedsList;
    private List<Follow> followList;
    private List<Follow> followerList;
}
