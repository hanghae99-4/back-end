package com.hanghae.instakilogram.repository;

import com.hanghae.instakilogram.entity.Feeds;
import com.hanghae.instakilogram.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedsRepository extends JpaRepository<Feeds, Long> {
    List<Feeds> findAllByOrderByCreatedAtDesc();

    Feeds findByMember(Member member);

    List<Feeds> findAllByMember(Member member);
}
