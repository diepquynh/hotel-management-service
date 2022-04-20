package vn.utc.hotelmanager.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.utc.hotelmanager.auth.user.model.User;

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
    private Set<ReceiptRoom> receiptRooms;

    @ManyToOne
    @JoinTable(
            name = "users_receipts",
            joinColumns = @JoinColumn(name = "receipt_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private User user;
}