package vn.utc.userservice.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "auth_id")
    private String authId;

    @Column(name = "email", length = 45, nullable = false, unique = true)
    private String email;

    @Column(name = "username", length = 45, nullable = false, unique = true)
    private String username;

    @Column(name = "status", length = 25, nullable = false)
    private String status;

    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;
}
