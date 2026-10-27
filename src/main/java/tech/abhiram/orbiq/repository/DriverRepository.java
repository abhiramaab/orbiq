package tech.abhiram.orbiq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.abhiram.orbiq.entity.Driver;
import tech.abhiram.orbiq.entity.enums.DriverStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByVehicleNumber(String vehicleNumber);

    Optional<Driver> findByPhoneNumber(String phoneNumber);

    List<Driver> findByStatus(DriverStatus status);

    List<Driver> findByCurrentH3IndexAndStatus(String currentH3Index, DriverStatus status);
}
