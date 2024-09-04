package com.Java_instagram_clone.domain.profile.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseProfile {

    private Integer profileNo;
    private String photo;
    private String birthday;
    private String gender;

}
