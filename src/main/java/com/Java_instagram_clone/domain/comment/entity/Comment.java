package com.Java_instagram_clone.domain.comment.entity;

import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private Member user;

    @ManyToOne(targetEntity = Feed.class)
    @JoinColumn(name = "FEED_ID")
    private Feed feed;

    @OneToMany(targetEntity = Like.class ,mappedBy = "comment")
    private List<Like> likes = new ArrayList<>();

    @Column()
    private String contents;


}
