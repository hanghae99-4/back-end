package com.hanghae.instakilogram.entity;

import com.hanghae.instakilogram.dto.request.FeedsRequestDto;
import com.hanghae.instakilogram.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feeds extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String imageUrl;

    @Column
    private Long heartNum;

    @Column
    private String contents;



    public void update(FeedsRequestDto feedsRequestDto) {

        this.contents = feedsRequestDto.getContent();

    }
}
