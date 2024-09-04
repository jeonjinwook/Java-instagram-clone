package com.Java_instagram_clone.domain.profile.entity;

import com.Java_instagram_clone.cmn.UserCmnDto;
import com.Java_instagram_clone.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestProfile extends UserCmnDto {

    private Integer profileNo;
    private String photo;
    private String birthday;
    private String gender;

    public Profile toEntity() {
        return Profile.builder()
                .id(profileNo)
                .photo(photo)
                .birthday(birthday)
                .gender(gender)
                .user(Member.builder().id(userNo).build())
                .build();
    }

}
