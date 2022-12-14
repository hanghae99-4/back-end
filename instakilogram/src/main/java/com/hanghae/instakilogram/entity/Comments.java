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
    @JoinColumn(name = "feeds_id", nullable = false)
    private Feeds feeds;


    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
