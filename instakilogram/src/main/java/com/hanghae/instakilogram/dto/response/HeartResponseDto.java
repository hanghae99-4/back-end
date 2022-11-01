package com.hanghae.instakilogram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HeartResponseDto {
    private String memberImage;
    private String ninkname;
}
