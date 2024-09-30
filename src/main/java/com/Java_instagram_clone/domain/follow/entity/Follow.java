package com.Java_instagram_clone.domain.follow.entity;

import com.Java_instagram_clone.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Follow")
@Entity(name = "Follow")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "FOLLOW_ID")
  private Integer id;

  @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
  @JsonIgnore
  private Member follower;

  @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
  @JsonIgnore
  private Member following;

  @Column()
  private Boolean checked;

}
