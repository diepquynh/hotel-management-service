package vn.utc.hotelmanager.auth.user.data;

import vn.utc.hotelmanager.auth.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "userDAO")
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
}
