package vn.utc.hotelmanager.hotel.data;

import org.springframework.stereotype.Component;
import vn.utc.hotelmanager.hotel.data.dto.request.RoomFilterRequestDTO;
import vn.utc.hotelmanager.hotel.model.Room;
import vn.utc.hotelmanager.hotel.model.RoomType;
import vn.utc.hotelmanager.hotel.model.RoomType_;
import vn.utc.hotelmanager.hotel.model.Room_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomRoomRepositoryImpl implements CustomRoomRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Room> findFilteredRooms(RoomFilterRequestDTO roomRequest) {
        List<Predicate> predicates = new ArrayList<>();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Room> query = cb.createQuery(Room.class);
        Root<Room> root = query.from(Room.class);
        Join<Room, RoomType> rooms = root.join(Room_.roomType, JoinType.INNER);

        if (roomRequest.getType() != null)
            predicates.add(cb.equal(rooms.get(RoomType_.NAME), roomRequest.getType()));

        if (roomRequest.getPriceFrom() != null)
            predicates.add(cb.greaterThanOrEqualTo(rooms.get(RoomType_.PRICE),
                    roomRequest.getPriceFrom()));

        if (roomRequest.getPriceTo() != null)
            predicates.add(cb.lessThanOrEqualTo(rooms.get(RoomType_.PRICE),
                    roomRequest.getPriceTo()));

        if (roomRequest.getSeatCountFrom() != null)
            predicates.add(cb.greaterThanOrEqualTo(rooms.get(RoomType_.CAPACITY),
                    roomRequest.getSeatCountFrom()));

        if (roomRequest.getSeatCountTo() != null)
            predicates.add(cb.lessThanOrEqualTo(rooms.get(RoomType_.CAPACITY),
                    roomRequest.getSeatCountTo()));

        query.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(query).getResultList();
    }


}
