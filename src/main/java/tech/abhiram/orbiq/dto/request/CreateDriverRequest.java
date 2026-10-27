package tech.abhiram.orbiq.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateDriverRequest(
        @NotBlank(message = "Driver name is required")
        String name,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{9,14}$", message = "Phone number must be a valid E.164 format")
        String phoneNumber,

        @NotBlank(message = "Vehicle number is required")
        String vehicleNumber
) {
}
