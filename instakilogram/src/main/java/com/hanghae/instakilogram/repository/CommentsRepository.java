package com.hanghae.instakilogram.repository;

import com.hanghae.instakilogram.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
