package vn.utc.userservice.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.userservice.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}
