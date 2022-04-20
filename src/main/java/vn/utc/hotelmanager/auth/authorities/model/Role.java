package vn.utc.hotelmanager.auth.authorities.model;

import vn.utc.hotelmanager.auth.user.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges;
}
