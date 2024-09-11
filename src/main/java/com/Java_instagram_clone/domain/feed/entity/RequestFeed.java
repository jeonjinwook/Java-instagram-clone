package com.Java_instagram_clone.domain.feed.entity;

import com.Java_instagram_clone.cmn.UserCmnDto;
import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.domain.profile.entity.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestFeed extends UserCmnDto {
    private Integer id;
    private Member user;
    private String contents;
    private String location;
    private Boolean isHide;

    public Feed toEntity() {
        return Feed.builder()
                .id(id)
                .contents(contents)
                .location(location)
                .isHide(isHide)
                .user(Member.builder().id(userNo).build())
                .build();
    }

}
