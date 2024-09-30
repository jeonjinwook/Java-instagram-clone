package com.Java_instagram_clone.domain.like.entity;

import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Likes")
@Entity(name = "Likes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Like {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "LIKE_ID")
  private Integer id;

  @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  @JsonIgnore
  private Member user;

  @ManyToOne(targetEntity = Feed.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "FEED_ID")
  @JsonIgnore
  private Feed feed;

  @ManyToOne(targetEntity = Comment.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "COMMENT_ID")
  @JsonIgnore
  private Comment comment;

  @Column()
  private Boolean active;

}
