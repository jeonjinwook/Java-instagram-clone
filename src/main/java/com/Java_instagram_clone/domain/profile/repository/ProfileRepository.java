package com.Java_instagram_clone.domain.profile.repository;

import com.Java_instagram_clone.domain.profile.entity.Profile;
import com.Java_instagram_clone.domain.profile.entity.ResponseProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {

    @Query(value = "SELECT NEW com.Java_instagram_clone.domain.profile.entity.ResponseProfile(t.id, t.photo, t.gender, t.birthday) " +
            "FROM Profile t where t.user.id = :userNo ")
    ResponseProfile findProfileById(@Param("userNo") long memberId);

}
