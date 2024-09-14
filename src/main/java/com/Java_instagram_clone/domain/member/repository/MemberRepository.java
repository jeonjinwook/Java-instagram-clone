package com.Java_instagram_clone.domain.member.repository;

import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.domain.member.entity.ResponseMember;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>,
    JpaSpecificationExecutor<Member> {

  @Query(
      value = "SELECT m.member_id " +
          "FROM follow f " +
          "JOIN member m ON f.follow_id = m.member_id " +
          "WHERE f.following_member_id = :userId",
      nativeQuery = true)
  List<ResponseMember> findFollowerAllByUserId(@Param("userId") Long userId);


}
