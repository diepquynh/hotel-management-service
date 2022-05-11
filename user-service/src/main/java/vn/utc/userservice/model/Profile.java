package vn.utc.userservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_name", length = 256)
    private String firstName;

    @Column(name = "last_name", length = 256)
    private String lastName;

    @Column(name = "address", length = 256)
    private String address;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "age")
    private Integer age;

    @Column(name = "image", length = 512)
    private String image;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "employee_id")
    private Integer employeeId;
}
