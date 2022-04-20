package vn.utc.hotelmanager.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "image", length = 256)
    private String image;

    @OneToMany(mappedBy = "room")
    private Set<ReceiptRoom> receiptRooms;

    @ManyToOne
    @JoinTable(
            name = "rooms_room_types",
            joinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "room_type_id", referencedColumnName = "id")
    )
    private RoomType roomType;
}