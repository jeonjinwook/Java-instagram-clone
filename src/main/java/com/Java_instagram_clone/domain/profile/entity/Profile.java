package com.Java_instagram_clone.domain.profile.entity;

import com.Java_instagram_clone.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member user;

  @Column(name = "photo")
  private String photo;

  @Column(name = "gender")
  private String gender;

  @Column(name = "birthday")
  private String birthday;


}
