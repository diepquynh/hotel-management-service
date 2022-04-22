package vn.utc.hotelmanager.hotel.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "receipts")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "created_date")
    private Instant created_date;

    @Column(name = "total_balance")
    private Double total_balance;

    @OneToMany(mappedBy = "receipt")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ReceiptRoom> receiptRooms;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "receipt", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserReceipt userReceipt;

    public void addTotalBalance(Double extraBalance) {
        total_balance += extraBalance;
    }
}