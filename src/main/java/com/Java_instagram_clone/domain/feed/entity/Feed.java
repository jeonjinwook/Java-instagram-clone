package com.Java_instagram_clone.domain.feed.entity;

import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Feed")
@Entity(name = "Feed")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feed {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "FEED_ID")
  private Integer id;

  @ManyToOne(targetEntity = Member.class)
  @JoinColumn(name = "MEMBER_ID")
  @JsonIgnore
  private Member user;

  @OneToMany(targetEntity = Comment.class, mappedBy = "feed", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Comment> comments;

  @OneToMany(targetEntity = Like.class, mappedBy = "feed", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @JsonIgnore
  private List<Like> likes;

  //    @Type(value = StringArrayType.class)
//    @Column(name = "files", columnDefinition = "text[]")
//    private String[] files;
  @Column()
  private String files;
  @Transient
  private String[] file;

  @Column()
  private String contents;

  @Column()
  private String location;

  @Column()
  private Boolean isHide;

}
