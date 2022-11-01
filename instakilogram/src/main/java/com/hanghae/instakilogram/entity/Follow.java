package com.hanghae.instakilogram.entity;

import com.hanghae.instakilogram.dto.request.FollowRequestDto;
import com.hanghae.instakilogram.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn()
    private Member fromMember;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member toMember;

}
