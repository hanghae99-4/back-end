package com.hanghae.instakilogram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {
    private List<String> memberIdList;
}
