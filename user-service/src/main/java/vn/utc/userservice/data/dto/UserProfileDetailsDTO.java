package vn.utc.userservice.data.dto;

import lombok.Data;
import vn.utc.userservice.model.Profile;

@Data
public class UserProfileDetailsDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private Integer age;
    private String image;
    private Boolean gender;
    private Integer employeeId;

    public UserProfileDetailsDTO(Profile profile) {
        setId(profile.getId());
        setFirstName(profile.getFirstName());
        setLastName(profile.getLastName());
        setAddress(profile.getAddress());
        setPhone(profile.getPhone());
        setAge(profile.getAge());
        setImage(profile.getImage());
        setGender(profile.getGender());
        setEmployeeId(profile.getEmployeeId());
    }
}
