package com.Java_instagram_clone.domain.feed.repository;

import com.Java_instagram_clone.domain.feed.entity.Feed;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>, JpaSpecificationExecutor<Feed> {

  ArrayList<Feed> findByUserId(@Param("userNo") long memberId);
}
