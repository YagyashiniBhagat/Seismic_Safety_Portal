package com.safehome.seismic_portal.service;

import com.safehome.seismic_portal.model.SeismicData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;

@Service
public class SeismicService {

    private final RestClient restClient;

    private static final Map<String, String> STATE_ZONE_MAP = new HashMap<>();
    private static final Map<String, String> STATE_SOIL_MAP = new HashMap<>();
    private static final Map<String, String> STATE_TERRAIN_MAP = new HashMap<>();
    private static final Map<String, double[]> STATE_COORDS_MAP = new HashMap<>();

    static {
        // ZONE V
        STATE_ZONE_MAP.put("Jammu and Kashmir", "Zone V (Very High Risk)");
        STATE_ZONE_MAP.put("Himachal Pradesh", "Zone V (Very High Risk)");
        STATE_ZONE_MAP.put("Uttarakhand", "Zone V (Very High Risk)");
        STATE_ZONE_MAP.put("Assam", "Zone V (Very High Risk)");
        STATE_ZONE_MAP.put("Meghalaya", "Zone V (Very High Risk)");
        STATE_ZONE_MAP.put("Manipur", "Zone V (Very High Risk)");
        STATE_ZONE_MAP.put("Nagaland", "Zone V (Very High Risk)");
        STATE_ZONE_MAP.put("Arunachal Pradesh", "Zone V (Very High Risk)");
        STATE_ZONE_MAP.put("Mizoram", "Zone V (Very High Risk)");
        STATE_ZONE_MAP.put("Andaman and Nicobar Islands", "Zone V (Very High Risk)");
        STATE_ZONE_MAP.put("Sikkim", "Zone V (Very High Risk)");

        // ZONE IV
        STATE_ZONE_MAP.put("Bihar", "Zone IV (High Risk)");
        STATE_ZONE_MAP.put("West Bengal", "Zone IV (High Risk)");
        STATE_ZONE_MAP.put("Delhi", "Zone IV (High Risk)");
        STATE_ZONE_MAP.put("Punjab", "Zone IV (High Risk)");
        STATE_ZONE_MAP.put("Haryana", "Zone IV (High Risk)");
        STATE_ZONE_MAP.put("Uttar Pradesh", "Zone IV (High Risk)");
        STATE_ZONE_MAP.put("Gujarat", "Zone IV (High Risk)");
        STATE_ZONE_MAP.put("Tripura", "Zone IV (High Risk)");
        STATE_ZONE_MAP.put("Ladakh", "Zone IV (High Risk)");

        // ZONE III
        STATE_ZONE_MAP.put("Maharashtra", "Zone III (Moderate Risk)");
        STATE_ZONE_MAP.put("Odisha", "Zone III (Moderate Risk)");
        STATE_ZONE_MAP.put("Chhattisgarh", "Zone III (Moderate Risk)");
        STATE_ZONE_MAP.put("Jharkhand", "Zone III (Moderate Risk)");
        STATE_ZONE_MAP.put("Rajasthan", "Zone III (Moderate Risk)");
        STATE_ZONE_MAP.put("Madhya Pradesh", "Zone III (Moderate Risk)");
        STATE_ZONE_MAP.put("Kerala", "Zone III (Moderate Risk)");
        STATE_ZONE_MAP.put("Tamil Nadu", "Zone III (Moderate Risk)");
        STATE_ZONE_MAP.put("Puducherry", "Zone III (Moderate Risk)");
        STATE_ZONE_MAP.put("Goa", "Zone III (Moderate Risk)");

        // ZONE II
        STATE_ZONE_MAP.put("Andhra Pradesh", "Zone II (Low Risk)");
        STATE_ZONE_MAP.put("Telangana", "Zone II (Low Risk)");
        STATE_ZONE_MAP.put("Karnataka", "Zone II (Low Risk)");
        STATE_ZONE_MAP.put("Chandigarh", "Zone II (Low Risk)");

        // SOIL TYPES
        STATE_SOIL_MAP.put("Uttarakhand", "Type II – Medium Alluvial/Silt");
        STATE_SOIL_MAP.put("Himachal Pradesh", "Type I – Rock / Hard Soil");
        STATE_SOIL_MAP.put("Jammu and Kashmir", "Type I – Rock / Hard Soil");
        STATE_SOIL_MAP.put("Delhi", "Type II – Alluvial Plains");
        STATE_SOIL_MAP.put("Uttar Pradesh", "Type II – Alluvial Plains");
        STATE_SOIL_MAP.put("Bihar", "Type II – Alluvial Plains");
        STATE_SOIL_MAP.put("Punjab", "Type II – Alluvial Plains");
        STATE_SOIL_MAP.put("Haryana", "Type II – Alluvial Plains");
        STATE_SOIL_MAP.put("West Bengal", "Type III – Soft Alluvial / River Delta");
        STATE_SOIL_MAP.put("Assam", "Type III – Soft Alluvial / River Delta");
        STATE_SOIL_MAP.put("Odisha", "Type III – Soft Alluvial / River Delta");
        STATE_SOIL_MAP.put("Maharashtra", "Type I – Hard Rock / Basalt");
        STATE_SOIL_MAP.put("Karnataka", "Type I – Granite / Hard Rock");
        STATE_SOIL_MAP.put("Telangana", "Type I – Granite / Hard Rock");
        STATE_SOIL_MAP.put("Andhra Pradesh", "Type I – Granite / Hard Rock");
        STATE_SOIL_MAP.put("Tamil Nadu", "Type II – Coastal Alluvial");
        STATE_SOIL_MAP.put("Kerala", "Type II – Coastal Alluvial");
        STATE_SOIL_MAP.put("Goa", "Type II – Coastal Alluvial");
        STATE_SOIL_MAP.put("Gujarat", "Type II – Coastal Alluvial");
        STATE_SOIL_MAP.put("Rajasthan", "Type I – Sandy / Arid Soil");
        STATE_SOIL_MAP.put("Madhya Pradesh", "Type II – Black Cotton Soil");
        STATE_SOIL_MAP.put("Chhattisgarh", "Type II – Black Cotton Soil");

        // TERRAIN
        STATE_TERRAIN_MAP.put("Uttarakhand", "Hilly / Himalayan Slope");
        STATE_TERRAIN_MAP.put("Himachal Pradesh", "Hilly / Himalayan Slope");
        STATE_TERRAIN_MAP.put("Jammu and Kashmir", "Hilly / Himalayan Slope");
        STATE_TERRAIN_MAP.put("Sikkim", "Hilly / Himalayan Slope");
        STATE_TERRAIN_MAP.put("Arunachal Pradesh", "Hilly / Himalayan Slope");
        STATE_TERRAIN_MAP.put("Assam", "Low-lying River Valley");
        STATE_TERRAIN_MAP.put("West Bengal", "Low-lying Delta Plain");
        STATE_TERRAIN_MAP.put("Odisha", "Low-lying Coastal Plain");
        STATE_TERRAIN_MAP.put("Kerala", "Coastal / Low-lying");
        STATE_TERRAIN_MAP.put("Tamil Nadu", "Coastal Plain");
        STATE_TERRAIN_MAP.put("Goa", "Coastal Rocky Terrain");
        STATE_TERRAIN_MAP.put("Maharashtra", "Coastal Rocky Terrain");
        STATE_TERRAIN_MAP.put("Gujarat", "Low Coastal Plain");
        STATE_TERRAIN_MAP.put("Rajasthan", "Arid Desert Plain");
        STATE_TERRAIN_MAP.put("Delhi", "Flat Urban Plain");
        STATE_TERRAIN_MAP.put("Punjab", "Flat Alluvial Plain");
        STATE_TERRAIN_MAP.put("Haryana", "Flat Alluvial Plain");
        STATE_TERRAIN_MAP.put("Uttar Pradesh", "Flat Alluvial Plain");
        STATE_TERRAIN_MAP.put("Bihar", "Flat Alluvial Plain");
        STATE_TERRAIN_MAP.put("Madhya Pradesh", "Deccan Plateau");
        STATE_TERRAIN_MAP.put("Chhattisgarh", "Deccan Plateau");
        STATE_TERRAIN_MAP.put("Karnataka", "Deccan Plateau");
        STATE_TERRAIN_MAP.put("Telangana", "Deccan Plateau");
        STATE_TERRAIN_MAP.put("Andhra Pradesh", "Deccan Plateau");

        // COORDINATES
        STATE_COORDS_MAP.put("Uttarakhand", new double[]{30.0668, 79.0193});
        STATE_COORDS_MAP.put("Himachal Pradesh", new double[]{31.1048, 77.1734});
        STATE_COORDS_MAP.put("Jammu and Kashmir", new double[]{33.7782, 76.5762});
        STATE_COORDS_MAP.put("Delhi", new double[]{28.6139, 77.2090});
        STATE_COORDS_MAP.put("Uttar Pradesh", new double[]{26.8467, 80.9462});
        STATE_COORDS_MAP.put("Bihar", new double[]{25.0961, 85.3131});
        STATE_COORDS_MAP.put("West Bengal", new double[]{22.5726, 88.3639});
        STATE_COORDS_MAP.put("Assam", new double[]{26.2006, 92.9376});
        STATE_COORDS_MAP.put("Maharashtra", new double[]{19.7515, 75.7139});
        STATE_COORDS_MAP.put("Gujarat", new double[]{22.2587, 71.1924});
        STATE_COORDS_MAP.put("Rajasthan", new double[]{27.0238, 74.2179});
        STATE_COORDS_MAP.put("Madhya Pradesh", new double[]{22.9734, 78.6569});
        STATE_COORDS_MAP.put("Karnataka", new double[]{15.3173, 75.7139});
        STATE_COORDS_MAP.put("Telangana", new double[]{17.3616, 78.4747});
        STATE_COORDS_MAP.put("Andhra Pradesh", new double[]{15.9129, 79.7400});
        STATE_COORDS_MAP.put("Tamil Nadu", new double[]{11.1271, 78.6569});
        STATE_COORDS_MAP.put("Kerala", new double[]{10.8505, 76.2711});
        STATE_COORDS_MAP.put("Odisha", new double[]{20.9517, 85.0985});
        STATE_COORDS_MAP.put("Punjab", new double[]{31.1471, 75.3412});
        STATE_COORDS_MAP.put("Haryana", new double[]{29.0588, 76.0856});
        STATE_COORDS_MAP.put("Chhattisgarh", new double[]{21.2787, 81.8661});
        STATE_COORDS_MAP.put("Jharkhand", new double[]{23.6102, 85.2799});
        STATE_COORDS_MAP.put("Goa", new double[]{15.2993, 74.1240});
        STATE_COORDS_MAP.put("Sikkim", new double[]{27.5330, 88.5122});
        STATE_COORDS_MAP.put("Meghalaya", new double[]{25.4670, 91.3662});
        STATE_COORDS_MAP.put("Manipur", new double[]{24.6637, 93.9063});
        STATE_COORDS_MAP.put("Nagaland", new double[]{26.1584, 94.5624});
        STATE_COORDS_MAP.put("Arunachal Pradesh", new double[]{28.2180, 94.7278});
        STATE_COORDS_MAP.put("Mizoram", new double[]{23.1645, 92.9376});
        STATE_COORDS_MAP.put("Tripura", new double[]{23.9408, 91.9882});
        STATE_COORDS_MAP.put("Andaman and Nicobar Islands", new double[]{11.7401, 92.6586});
    }

    public SeismicService() {
        this.restClient = RestClient.builder()
                .requestFactory(new org.springframework.http.client.JdkClientHttpRequestFactory(
                        java.net.http.HttpClient.newBuilder()
                                .version(java.net.http.HttpClient.Version.HTTP_1_1)
                                .build()
                ))
                .build();
    }

    public SeismicData getSafetyProfile(String pincode) {
        try {
            String url = "https://api.postalpincode.in/pincode/" + pincode;

            List<?> response = restClient.get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(List.class);

            if (response == null || response.isEmpty()) return null;

            Map<?, ?> firstResult = (Map<?, ?>) response.get(0);
            String status = (String) firstResult.get("Status");
            if (!"Success".equals(status)) return null;

            List<?> postOffices = (List<?>) firstResult.get("PostOffice");
            if (postOffices == null || postOffices.isEmpty()) return null;

            Map<?, ?> postOffice = (Map<?, ?>) postOffices.get(0);
            String district = (String) postOffice.get("District");
            String state = (String) postOffice.get("State");
            String name = (String) postOffice.get("Name");
            String locationName = name + ", " + district;

            String zone = STATE_ZONE_MAP.getOrDefault(state, "Zone III (Moderate Risk)");
            String soil = STATE_SOIL_MAP.getOrDefault(state, "Type II – Mixed Alluvial Soil");
            String terrain = STATE_TERRAIN_MAP.getOrDefault(state, "Mixed Terrain");

            double[] coords = STATE_COORDS_MAP.getOrDefault(state, new double[]{20.5937, 78.9629});

            SeismicData data = new SeismicData(
                    pincode, district, locationName, zone, soil, terrain, ""
            );
            data.setLatitude(coords[0]);
            data.setLongitude(coords[1]);
            return data;

        } catch (Exception e) {
            System.out.println("=== SEISMIC SERVICE ERROR: " + e.getClass().getSimpleName() + " — " + e.getMessage() + " ===");
            e.printStackTrace();
            return null;
        }
    }
}