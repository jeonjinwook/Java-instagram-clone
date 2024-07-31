package com.Java_instagram_clone.domain.member.entity;

import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.follow.entity.Follow;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.profile.entity.Profile;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Table(name = "Member")
@Entity(name = "Member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String password;

    @OneToMany(targetEntity = Feed.class, mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Feed> feeds;

    @OneToMany(targetEntity = Comment.class, mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Comment> comments;

    @OneToMany(targetEntity = Like.class, mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Like> likes;

    @OneToMany(targetEntity = Profile.class, mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Profile> profile;

    @OneToMany(targetEntity = Follow.class, mappedBy = "follower", cascade = CascadeType.PERSIST)
    private List<Follow> followers;

    @OneToMany(targetEntity = Follow.class, mappedBy = "following", cascade = CascadeType.PERSIST)
    private List<Follow> followings;
}
