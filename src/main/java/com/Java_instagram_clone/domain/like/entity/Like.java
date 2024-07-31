package com.Java_instagram_clone.domain.like.entity;

import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "MEMBER_ID")
    private Member user;

    @ManyToOne(targetEntity = Feed.class)
    @JoinColumn(name = "FEED_ID")
    private Feed feed;

    @ManyToOne(targetEntity = Comment.class)
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    @Column()
    private Boolean active;

}
