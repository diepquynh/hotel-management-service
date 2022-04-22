package vn.utc.hotelmanager.hotel.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "receipts_rooms")
public class ReceiptRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "receiptRoom")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ReceiptRoomService> receiptRoomServices;

    @Column(name = "arrival_time")
    private Instant arrivalTime;

    @Column(name = "leave_time")
    private Instant leaveTime;

    @Column(name = "capacity")
    private Integer capacity;

    public boolean isRoomReceiptActive() {
        Instant currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        return currentTime.isAfter(arrivalTime) && currentTime.isBefore(leaveTime);
    }

    public void addReceiptRoomService(ReceiptRoomService receiptRoomService) {
        receiptRoomServices.add(receiptRoomService);
    }
}