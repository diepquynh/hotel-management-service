package vn.utc.userservice.data.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private Integer age;
    private String image;
    private Boolean gender;
}
