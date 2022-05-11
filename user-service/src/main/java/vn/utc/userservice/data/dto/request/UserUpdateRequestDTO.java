package vn.utc.userservice.data.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequestDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private Integer age;
    private String image;
    private Boolean gender;
}
