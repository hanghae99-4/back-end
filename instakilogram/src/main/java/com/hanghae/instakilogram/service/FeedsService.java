package com.hanghae.instakilogram.service;

import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.repository.FeedsRepository;

import java.util.Optional;

public class FeedsService {

    private FeedsRepository feedsRepository;

    public Feeds isPresentFeed(Long id) {
        Optional<Feeds> optionalFeed = feedsRepository.findById(id);
        return optionalFeed.orElse(null);
    }
}
