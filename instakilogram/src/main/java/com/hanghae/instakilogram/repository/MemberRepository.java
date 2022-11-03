package com.hanghae.instakilogram.repository;

import com.hanghae.instakilogram.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByMemberId(String userId);

    List<Member> findByMemberIdContaining(String keyword);
}
