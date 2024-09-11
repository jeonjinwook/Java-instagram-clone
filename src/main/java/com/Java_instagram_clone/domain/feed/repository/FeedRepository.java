package com.Java_instagram_clone.domain.feed.repository;

import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.feed.entity.ResponseFeed;
import com.Java_instagram_clone.domain.profile.entity.Profile;
import com.Java_instagram_clone.domain.profile.entity.ResponseProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long>, JpaSpecificationExecutor<Feed> {

    public ArrayList<Feed> findByUserId(@Param("userNo") long memberId);


}
