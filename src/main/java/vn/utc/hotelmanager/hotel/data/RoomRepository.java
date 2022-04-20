package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.utc.hotelmanager.hotel.model.Room;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer>, CustomRoomRepository {
    Optional<Room> findByName(String name);

    @Query(value = "select " +
            "r.* from rooms r " +
            "join receipts_rooms rr on r.id = rr.room_id " +
            "having rr.arrival_time > :endTime " +
            "and rr.leave_time < :startTime", nativeQuery = true)
    List<Room> findAvailableRoomBetweenTime(@Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
}