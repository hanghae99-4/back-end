package com.hanghae.instakilogram.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class MemberUpdateRequestDto {
    private String nickname;
    private MultipartFile memberImage;
    private String introduce;
}
