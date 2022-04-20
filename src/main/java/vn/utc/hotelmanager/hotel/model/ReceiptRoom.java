package vn.utc.hotelmanager.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
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

    @ManyToMany(mappedBy = "receiptRooms")
    private Set<Service> services;

    @Column(name = "arrival_time")
    private Instant arrivalTime;

    @Column(name = "leave_time")
    private Instant leaveTime;
}