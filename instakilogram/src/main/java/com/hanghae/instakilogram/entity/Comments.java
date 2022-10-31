package com.hanghae.instakilogram.entity;

import com.hanghae.instakilogram.dto.request.CommentsRequestDto;
import com.hanghae.instakilogram.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comments extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String contents;

    @ManyToOne
    @JoinColumn(name = "feeds_id")
    private Feeds feeds;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public boolean validateMember(Member member) {
        return !this.member.getMemberId().equals(member.getMemberId());
    }


    public void update(CommentsRequestDto commentsRequestDto) {
        this.contents = commentsRequestDto.getContents();
    }
}
