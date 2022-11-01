package com.hanghae.instakilogram.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class FeedsRequestDto {

    private MultipartFile image;
    private String contents;
}
