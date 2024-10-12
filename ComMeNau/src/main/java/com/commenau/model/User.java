package com.commenau.model;

import com.commenau.log.Logable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Logable {
    private long id;
    private int roleId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;
    private String status;
    private int numLoginFailInDay;
    private Timestamp updatedAt;
    private Timestamp createdAt;

    public String fullName() {
        return lastName + " " + firstName;
    }

    @Override
    public Logable getData() {
        return User.builder().id(this.id).lastName(this.lastName).firstName(this.firstName).username(this.username).build();
    }
}