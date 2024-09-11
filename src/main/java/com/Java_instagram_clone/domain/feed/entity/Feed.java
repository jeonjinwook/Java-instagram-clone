package com.Java_instagram_clone.domain.feed.entity;

import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

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
    @JsonIgnore
    private Member user;

    @OneToMany(targetEntity = Comment.class, mappedBy = "feed", cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<Comment> comments;

    @OneToMany(targetEntity = Like.class, mappedBy = "feed", cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
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
