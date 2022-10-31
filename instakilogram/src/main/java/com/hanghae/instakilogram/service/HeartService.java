package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.dto.request.HeartRequestDto;
import com.hanghae.instakilogram.dto.response.ResponseDto;
import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Heart;
import com.hanghae.instakilogram.entity.Member;
import com.hanghae.instakilogram.repository.HeartRepository;
import com.hanghae.instakilogram.util.Verification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final Verification verification;
    public ResponseDto<?> updateHeart(Long feedId, Member member) {

        Feeds feeds = verification.getCurrentFeeds(feedId);

        Heart findHeart = heartRepository.findByMemberAndFeeds(member,feeds).orElse(null);

        if(findHeart == null){
            HeartRequestDto heartRequestDto = new HeartRequestDto(member, feeds);
            Heart heart = new Heart(heartRequestDto);
            heartRepository.save(heart);
            return ResponseDto.success(true);
        }else{
            heartRepository.deleteById(findHeart.getId());
            return ResponseDto.success(false);
        }
    }
}
