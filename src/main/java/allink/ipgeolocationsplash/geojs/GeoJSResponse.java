package allink.ipgeolocationsplash.geojs;

import lombok.Data;

@Data
public class GeoJSResponse {
    private final float latitude;
    private final float longitude;
    private final String city;
    private final String ip;
    private final String country;
}
