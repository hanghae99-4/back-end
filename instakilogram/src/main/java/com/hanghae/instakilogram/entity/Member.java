package com.hanghae.instakilogram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.instakilogram.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends TimeStamped {

    @Id
    private String memberId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column()
    private String memberImage;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Authority authority;
}
