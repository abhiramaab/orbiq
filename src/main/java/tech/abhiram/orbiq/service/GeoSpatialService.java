package tech.abhiram.orbiq.service;

import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
import com.uber.h3core.util.LatLng;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoSpatialService {

    public static final int DEFAULT_RESOLUTION = 8;

    private final H3Core h3Core;

    /**
     * Converts GPS coordinates (latitude, longitude) to an H3 Hexagon Address string
     * using the default city-dispatch resolution (Resolution 8 ~460m radius).
     */
    public String latLngToH3Index(double latitude, double longitude) {
        return latLngToH3Index(latitude, longitude, DEFAULT_RESOLUTION);
    }

    /**
     * Converts GPS coordinates (latitude, longitude) to an H3 Hexagon Address string
     * at a specific resolution (0-15).
     */
    public String latLngToH3Index(double latitude, double longitude, int resolution) {
        return h3Core.latLngToCellAddress(latitude, longitude, resolution);
    }

    /**
     * Retrieves all concentric hexagonal neighbor cells up to distance k (k-Ring expansion).
     * k = 0: origin cell (1 cell)
     * k = 1: origin + 6 immediate neighbors (7 cells)
     * k = 2: 19 cells total
     */
    public List<String> getKRingNeighbors(String h3Index, int k) {
        if (!isValidCell(h3Index)) {
            throw new IllegalArgumentException("Invalid H3 index: " + h3Index);
        }
        return h3Core.gridDisk(h3Index, k);
    }

    /**
     * Validates whether a given string is a valid H3 hexagon index.
     */
    public boolean isValidCell(String h3Index) {
        if (h3Index == null || h3Index.isBlank()) {
            return false;
        }
        try {
            return h3Core.isValidCell(h3Index);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Calculates the great-circle surface distance between two GPS points in meters.
     */
    public double calculateDistanceMeters(double lat1, double lng1, double lat2, double lng2) {
        LatLng origin = new LatLng(lat1, lng1);
        LatLng destination = new LatLng(lat2, lng2);
        return h3Core.greatCircleDistance(origin, destination, LengthUnit.m);
    }

    /**
     * Resolves the center GPS coordinates (latitude, longitude) of a given H3 hexagon cell.
     */
    public LatLng h3IndexToLatLng(String h3Index) {
        if (!isValidCell(h3Index)) {
            throw new IllegalArgumentException("Invalid H3 index: " + h3Index);
        }
        return h3Core.cellToLatLng(h3Index);
    }
}
