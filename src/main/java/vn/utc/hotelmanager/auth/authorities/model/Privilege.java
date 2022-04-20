package vn.utc.hotelmanager.auth.authorities.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "privileges", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Role> roles;
}
