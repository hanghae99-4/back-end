package com.hanghae.instakilogram.dto.response;

import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FeedsResponseDto {
    private String memberId;
    private String username;
    private String memberImage;
    private String introduce;
    private String nickname;
    private List<FeedsListResponseDto> feedsList;
    private List<Follow> followList;
    private List<Follow> followerList;
}
