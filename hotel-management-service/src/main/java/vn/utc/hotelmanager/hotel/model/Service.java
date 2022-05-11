package vn.utc.hotelmanager.hotel.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ReceiptRoomService> receiptRoomServices;
}