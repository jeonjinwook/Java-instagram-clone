package com.Java_instagram_clone.domain.comment.entity;

import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.member.entity.ResponseMember;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseComment {

  private Integer id;
  private ResponseMember user;
  private Feed feed;
  private List<Like> likes = new ArrayList<>();
  private String contents;


}
