package vn.utc.clients.user;

import lombok.Data;

@Data
public class UserProfile {
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private Integer age;
    private String image;
    private Boolean gender;
    private Integer employeeId;
}
