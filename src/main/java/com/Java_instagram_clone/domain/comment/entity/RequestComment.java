package com.Java_instagram_clone.domain.comment.entity;

import com.Java_instagram_clone.cmn.UserCmnDto;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestComment extends UserCmnDto {

  private Integer id;
  private Member user;
  private long feedId;
  private List<Like> likes = new ArrayList<>();
  private String contents;


}
