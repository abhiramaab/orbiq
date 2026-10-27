package tech.abhiram.orbiq.dto.request;

import jakarta.validation.constraints.NotNull;
import tech.abhiram.orbiq.entity.enums.DriverStatus;

public record UpdateDriverStatusRequest(
        @NotNull(message = "Driver status is required")
        DriverStatus status
) {
}
