package com.hanghae.instakilogram.repository;


import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Heart;
import com.hanghae.instakilogram.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {


    Optional<Heart> findByMemberAndFeeds(Member member, Feeds feeds);

    List<Heart> findByFeeds(Feeds feeds);

    Long countByFeeds(Feeds feeds);
}
