package com.Java_instagram_clone.domain.feed.entity;

import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private Member user;

    @OneToMany(targetEntity = Comment.class, mappedBy = "feed", cascade = CascadeType.PERSIST)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(targetEntity = Like.class, mappedBy = "feed", cascade = CascadeType.PERSIST)
    private List<Like> likes = new ArrayList<>();

    @Column()
    private String contents;

    @Column()
    private String location;

    @Column()
    private Boolean isHide;

}
