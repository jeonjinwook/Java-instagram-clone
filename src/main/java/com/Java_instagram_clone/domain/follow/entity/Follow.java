package com.Java_instagram_clone.domain.follow.entity;

import com.Java_instagram_clone.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(targetEntity = Member.class)
    private Member follower;

    @ManyToOne(targetEntity = Member.class)
    private Member following;

    @Column()
    private Boolean checked;

}
