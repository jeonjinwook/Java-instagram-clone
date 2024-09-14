package com.Java_instagram_clone.domain.member.entity;

import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.follow.entity.Follow;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.profile.entity.Profile;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMember {

  private long id;
  private String phoneNumber;
  private String email;
  private String name;
  private String accountName;
  private List<Feed> feeds;
  private List<Comment> comments;
  private List<Like> likes;
  private List<Profile> profile;
  private List<Follow> follower;
  private List<Follow> following;

}
