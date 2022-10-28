package com.hanghae.instakilogram.repository;

import com.hanghae.instakilogram.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {
}
