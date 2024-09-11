package com.Java_instagram_clone.domain.feed.entity;

import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.domain.member.entity.ResponseMember;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFeed {

     private Integer id;
     private ResponseMember user;
     private List<Comment> comments;
     private List<Like> likes;
     private String[] files;
     private String contents;
     private String location;
     private Boolean isHide;
}
