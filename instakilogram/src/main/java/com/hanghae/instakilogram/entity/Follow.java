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
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member_id", nullable = false)
    private Member toMember;

    @Builder
    public Follow(FollowRequestDto followRequestDto) {
        this.fromMember = followRequestDto.getFromMember();
        this.toMember = followRequestDto.getToMember();
    }
}
