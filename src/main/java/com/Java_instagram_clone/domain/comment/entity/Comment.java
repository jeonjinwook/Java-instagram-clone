package com.Java_instagram_clone.domain.comment.entity;

import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Comment")
@Entity(name = "Comment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "COMMENT_ID")
  private Integer id;

  @ManyToOne(targetEntity = Member.class)
  @JoinColumn(name = "MEMBER_ID")
  @JsonIgnore
  private Member user;

  @ManyToOne(targetEntity = Feed.class)
  @JoinColumn(name = "FEED_ID")
  @JsonIgnore
  private Feed feed;

  @OneToMany(targetEntity = Like.class, mappedBy = "comment")
  @JsonIgnore
  private List<Like> likes = new ArrayList<>();

  @Column()
  private String contents;


}
