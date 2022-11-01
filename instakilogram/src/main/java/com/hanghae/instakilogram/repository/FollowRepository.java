package com.hanghae.instakilogram.repository;

import com.hanghae.instakilogram.entity.Follow;
import com.hanghae.instakilogram.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> findByFromMemberAndToMember_memberId(Member member, String feedsId);

    List<Follow> findAllByFromMember(Member member);
}
