package tech.abhiram.orbiq.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.abhiram.orbiq.entity.enums.DriverStatus;

import java.time.Instant;

@Entity
@Table(name = "driver")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;

    private Double currentLatitude;

    private Double currentLongitude;

    private String currentH3Index;

    private Instant lastLocationUpdate;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = Instant.now();
        if (this.status == null) {
            this.status = DriverStatus.OFFLINE;
        }
    }


}
