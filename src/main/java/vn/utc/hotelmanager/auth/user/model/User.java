package vn.utc.hotelmanager.auth.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import vn.utc.hotelmanager.auth.authorities.model.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vn.utc.hotelmanager.hotel.model.Booking;
import vn.utc.hotelmanager.hotel.model.Review;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "email", length = 45, nullable = false, unique = true)
    private String email;

    @Column(name = "username", length = 45, nullable = false, unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @Column(name = "enabled", length = 4, nullable = false)
    private Boolean enabled;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = 15)
    private Provider provider;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Booking> bookings;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Review> reviews;
}
