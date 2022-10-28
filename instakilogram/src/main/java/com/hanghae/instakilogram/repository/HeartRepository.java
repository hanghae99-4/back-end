package com.hanghae.instakilogram.repository;

import com.hanghae.instakilogram.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
}
