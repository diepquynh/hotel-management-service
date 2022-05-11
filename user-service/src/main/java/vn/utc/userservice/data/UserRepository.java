package vn.utc.userservice.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
