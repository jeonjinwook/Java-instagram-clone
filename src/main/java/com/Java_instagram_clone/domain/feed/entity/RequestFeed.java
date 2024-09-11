package com.Java_instagram_clone.domain.feed.entity;

import com.Java_instagram_clone.cmn.UserCmnDto;
import com.Java_instagram_clone.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
