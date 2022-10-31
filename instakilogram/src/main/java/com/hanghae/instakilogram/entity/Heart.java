package com.hanghae.instakilogram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.instakilogram.dto.request.HeartRequestDto;
import com.hanghae.instakilogram.dto.response.HeartResponseDto;
import com.hanghae.instakilogram.util.TimeStamped;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Heart extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feeds_id")
    @JsonIgnore
    private Feeds feeds;

    @Builder
    public Heart(HeartRequestDto requestDto) {
        this.member = requestDto.getMember();
        this.feeds = requestDto.getFeeds();
    }
}
