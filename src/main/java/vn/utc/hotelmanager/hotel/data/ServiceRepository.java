package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.hotelmanager.hotel.model.Service;

public interface ServiceRepository extends JpaRepository<Service, Integer>, CustomServiceRepository {
}