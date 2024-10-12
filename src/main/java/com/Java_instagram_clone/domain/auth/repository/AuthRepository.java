package com.Java_instagram_clone.domain.auth.repository;

import com.Java_instagram_clone.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);

  Optional<Member> findByAccountName(String accountName);

  Member findById(long memberId);

  boolean existsByEmail(String email);
}
