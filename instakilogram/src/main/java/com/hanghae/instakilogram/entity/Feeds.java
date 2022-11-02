package com.hanghae.instakilogram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.instakilogram.dto.request.FeedsRequestDto;
import com.hanghae.instakilogram.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @Column(nullable = false)
    private String feedImage;

    @Column
    private String contents;

    @OneToMany(mappedBy = "feeds", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comments> commentsList = new ArrayList<>();

    @OneToMany(mappedBy = "feeds", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Heart> heartList = new ArrayList<>();
}
