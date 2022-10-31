package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.repository.FeedsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedsService {

    private FeedsRepository feedsRepository;

    @Transactional(readOnly = true)
    public Feeds isPresentFeed(Long id) {
        Optional<Feeds> optionalFeed = feedsRepository.findById(id);
        return optionalFeed.orElse(null);
    }
}
