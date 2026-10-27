package tech.abhiram.orbiq.dto.response;

import tech.abhiram.orbiq.entity.Driver;
import tech.abhiram.orbiq.entity.enums.DriverStatus;

import java.time.Instant;

public record DriverResponse(
        Long id,
        String name,
        String phoneNumber,
        String vehicleNumber,
        DriverStatus status,
        Double currentLatitude,
        Double currentLongitude,
        String currentH3Index,
        Instant lastLocationUpdate,
        Instant createdAt
) {
    public static DriverResponse fromEntity(Driver driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getName(),
                driver.getPhoneNumber(),
                driver.getVehicleNumber(),
                driver.getStatus(),
                driver.getCurrentLatitude(),
                driver.getCurrentLongitude(),
                driver.getCurrentH3Index(),
                driver.getLastLocationUpdate(),
                driver.getCreatedAt()
        );
    }
}
