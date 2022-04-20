package vn.utc.hotelmanager.auth.authorities.data;

import vn.utc.hotelmanager.auth.authorities.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
