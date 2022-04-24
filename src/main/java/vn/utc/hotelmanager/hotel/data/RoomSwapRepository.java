package vn.utc.hotelmanager.hotel.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.utc.hotelmanager.hotel.model.RoomSwap;

import java.util.List;

public interface RoomSwapRepository extends JpaRepository<RoomSwap, Integer> {

    @Query(value = "select rs.* from room_swaps rs " +
            "inner join bookings b on b.receipt_id = rs.receipt_id " +
            "where b.id = :bookingId", nativeQuery = true)
    List<RoomSwap> findRoomSwapsByBookingId(@Param("bookingId") Integer bookingId);
}