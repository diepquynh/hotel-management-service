package vn.utc.hotelmanager.hotel.data;

import org.springframework.stereotype.Component;
import vn.utc.hotelmanager.hotel.data.dto.request.HotelServiceFilterRequestDTO;
import vn.utc.hotelmanager.hotel.model.Service;
import vn.utc.hotelmanager.hotel.model.Service_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomServiceRepositoryImpl implements CustomServiceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Service> findFilteredServices(HotelServiceFilterRequestDTO serviceRequest) {
        List<Predicate> predicates = new ArrayList<>();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Service> query = cb.createQuery(Service.class);
        Root<Service> root = query.from(Service.class);

        if (serviceRequest.getType() != null)
            predicates.add(cb.equal(root.get(Service_.TYPE), serviceRequest.getType()));

        if (serviceRequest.getPriceFrom() != null)
            predicates.add(cb.greaterThanOrEqualTo(
                    root.get(Service_.PRICE), serviceRequest.getPriceFrom()
            ));

        if (serviceRequest.getPriceTo() != null)
            predicates.add(cb.lessThanOrEqualTo(
                    root.get(Service_.PRICE), serviceRequest.getPriceTo()
            ));

        query.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(query).getResultList();
    }
}
