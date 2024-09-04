package com.Java_instagram_clone.domain.profile.entity;

import com.Java_instagram_clone.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Table(name = "Profile")
@Entity(name = "Profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "member_id")
    private Member user;

    @Column(name="photo")
    private String photo;

    @Column(name="gender")
    private String gender;

    @Column(name="birthday")
    private String birthday;


}
