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

    @Query(value = "select r.*, rrt.room_type_id from rooms r " +
            "inner join rooms_room_types rrt on rrt.room_id = r.id " +
            "inner join receipts_rooms rr on r.id = rr.room_id " +
            "and (rr.arrival_time > :endTime " +
            "or rr.leave_time < :startTime) " +
            "union all " +
            "select r.*, rrt.room_type_id from rooms r " +
            "inner join rooms_room_types rrt on rrt.room_id = r.id " +
            "left join receipts_rooms rr on r.id = rr.room_id " +
            "where rr.room_id is null", nativeQuery = true)
    List<Room> findAvailableRoomBetweenTime(@Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
}