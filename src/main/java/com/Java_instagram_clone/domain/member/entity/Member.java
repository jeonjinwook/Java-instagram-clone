package com.Java_instagram_clone.domain.member.entity;

import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.follow.entity.Follow;
import com.Java_instagram_clone.domain.like.entity.Like;
import com.Java_instagram_clone.domain.profile.entity.Profile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "Member")
@Entity(name = "Member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {

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

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
