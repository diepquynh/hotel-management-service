package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.hotelmanager.hotel.model.Service;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Integer>, CustomServiceRepository {
    Optional<Service> findByName(String name);
}