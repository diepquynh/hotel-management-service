package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.utc.hotelmanager.hotel.model.RoomType;

import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
    Optional<RoomType> findByName(String name);
}