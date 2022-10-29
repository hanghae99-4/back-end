package com.hanghae.instakilogram.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    private String memberId;

    private String username;

    private String nickname;

    private String password;
}
