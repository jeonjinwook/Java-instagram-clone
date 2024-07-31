package com.Java_instagram_clone.domain.profile.entity;

import com.Java_instagram_clone.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Profile")
@Entity(name = "Profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "memberId")
    private Member user;

    @Column()
    private String photo;

    @Column()
    private String gender;

    @Column()
    private String birthday;


}
