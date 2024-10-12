package com.commenau.mapper;

import com.commenau.model.GooglePojo;
import com.commenau.model.User;

public class UserMapper {
    public static User toModel(GooglePojo pojo) {
        return User.builder().email(pojo.getEmail()).firstName(pojo.getFamily_name())
                .lastName(pojo.getGiven_name()).build();
    }

    public static User toModel(com.restfb.types.User user) {
        return User.builder().email(user.getEmail()).firstName(user.getFirstName()).lastName(user.getLastName()).build();
    }

}
