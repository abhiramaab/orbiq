package tech.abhiram.orbiq.service;

import com.uber.h3core.H3Core;
import com.uber.h3core.util.LatLng;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeoSpatialServiceTest {

    private GeoSpatialService geoSpatialService;

    // Bangalore MG Road coordinates
    private static final double BANGALORE_LAT = 12.9716;
    private static final double BANGALORE_LNG = 77.5946;

    // Indiranagar, Bangalore coordinates (~4.5 km away)
    private static final double INDIRANAGAR_LAT = 12.9784;
    private static final double INDIRANAGAR_LNG = 77.6408;

    @BeforeEach
    void setUp() throws IOException {
        H3Core h3Core = H3Core.newInstance();
        geoSpatialService = new GeoSpatialService(h3Core);
    }

    @Test
    @DisplayName("Should convert LatLng to valid H3 hex string at resolution 8")
    void shouldConvertLatLngToH3Index() {
        String h3Index = geoSpatialService.latLngToH3Index(BANGALORE_LAT, BANGALORE_LNG);

        assertThat(h3Index).isNotNull().isNotEmpty();
        assertThat(geoSpatialService.isValidCell(h3Index)).isTrue();
    }

    @Test
    @DisplayName("k=0 ring should contain only the origin hexagon")
    void shouldReturnOriginForKZero() {
        String h3Index = geoSpatialService.latLngToH3Index(BANGALORE_LAT, BANGALORE_LNG);
        List<String> ring = geoSpatialService.getKRingNeighbors(h3Index, 0);

        assertThat(ring).hasSize(1).contains(h3Index);
    }

    @Test
    @DisplayName("k=1 ring should return exactly 7 hexagons (1 center + 6 equidistant neighbors)")
    void shouldReturnSevenCellsForKOne() {
        String h3Index = geoSpatialService.latLngToH3Index(BANGALORE_LAT, BANGALORE_LNG);
        List<String> ring = geoSpatialService.getKRingNeighbors(h3Index, 1);

        assertThat(ring).hasSize(7).contains(h3Index);
    }

    @Test
    @DisplayName("k=2 ring should return exactly 19 hexagons (1 + 6 + 12)")
    void shouldReturnNineteenCellsForKTwo() {
        String h3Index = geoSpatialService.latLngToH3Index(BANGALORE_LAT, BANGALORE_LNG);
        List<String> ring = geoSpatialService.getKRingNeighbors(h3Index, 2);

        assertThat(ring).hasSize(19).contains(h3Index);
    }

    @Test
    @DisplayName("Should accurately calculate great-circle distance between two locations")
    void shouldCalculateAccurateDistance() {
        double distanceMeters = geoSpatialService.calculateDistanceMeters(
                BANGALORE_LAT, BANGALORE_LNG,
                INDIRANAGAR_LAT, INDIRANAGAR_LNG
        );

        // Distance between MG Road and Indiranagar is roughly ~5000 to 5200 meters
        assertThat(distanceMeters).isBetween(4800.0, 5400.0);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid H3 string")
    void shouldThrowForInvalidH3() {
        assertThatThrownBy(() -> geoSpatialService.getKRingNeighbors("invalid-h3-string", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid H3 index");
    }

    @Test
    @DisplayName("Should resolve center coordinates from H3 index accurately")
    void shouldResolveCenterCoordinates() {
        String h3Index = geoSpatialService.latLngToH3Index(BANGALORE_LAT, BANGALORE_LNG);
        LatLng center = geoSpatialService.h3IndexToLatLng(h3Index);

        // Coordinates of hexagon center should be within ~500m of original coordinate
        assertThat(center.lat).isCloseTo(BANGALORE_LAT, org.assertj.core.data.Offset.offset(0.01));
        assertThat(center.lng).isCloseTo(BANGALORE_LNG, org.assertj.core.data.Offset.offset(0.01));
    }
}
