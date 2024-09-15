package com.Java_instagram_clone.domain.member.repository;

import com.Java_instagram_clone.domain.follow.entity.Follow;
import com.Java_instagram_clone.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>,
    JpaSpecificationExecutor<Member> {

  @Query("SELECT f FROM Follow f WHERE f.follower.id = :userId")
  List<Follow> findFollowerAllByUserId(@Param("userId") Long userId);


}
