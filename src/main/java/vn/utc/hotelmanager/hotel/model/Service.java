package vn.utc.hotelmanager.hotel.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "type", nullable = false, length = 45)
    private String type;

    @Column(name = "image", length = 256)
    private String image;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "receipts_rooms_services",
            joinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "receipt_room_id", referencedColumnName = "id")
    )
    private Set<ReceiptRoom> receiptRooms;
}