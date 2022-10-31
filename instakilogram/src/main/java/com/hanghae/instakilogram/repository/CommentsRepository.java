package com.hanghae.instakilogram.repository;

import com.hanghae.instakilogram.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findAllByFeedId(Long feedId);
}
