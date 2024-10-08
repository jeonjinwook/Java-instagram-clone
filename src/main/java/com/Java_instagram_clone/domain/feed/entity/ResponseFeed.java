package com.Java_instagram_clone.domain.feed.entity;

import com.Java_instagram_clone.domain.comment.entity.ResponseComment;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.member.entity.ResponseMember;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFeed {

  private Integer id;
  private ResponseMember user;
  private List<ResponseComment> comments;
  private List<Like> likes;
  private String[] files;
  private String contents;
  private String location;
  private Boolean isHide;
}
