package com.safehome.seismic_portal.service;

import com.safehome.seismic_portal.model.SeismicData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class SeismicService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String POSTAL_API_URL = "https://api.postalpincode.in/pincode/";

    private static final Map<String, String> TOWN_ZONE_MAP = new HashMap<>();

    static {
        // --- ZONE V (Very High Risk - Z = 0.36g) ---
        String zoneV = "Zone V (Very High Risk - Z = 0.36g)";
        TOWN_ZONE_MAP.put("BHUJ", zoneV);
        TOWN_ZONE_MAP.put("KUTCH", zoneV);
        TOWN_ZONE_MAP.put("GANDHIDHAM", zoneV);
        TOWN_ZONE_MAP.put("ANJAR", zoneV);
        TOWN_ZONE_MAP.put("GUWAHATI", zoneV);
        TOWN_ZONE_MAP.put("SILCHAR", zoneV);
        TOWN_ZONE_MAP.put("DIBRUGARH", zoneV);
        TOWN_ZONE_MAP.put("JORHAT", zoneV);
        TOWN_ZONE_MAP.put("TEZPUR", zoneV);
        TOWN_ZONE_MAP.put("IMPHAL", zoneV);
        TOWN_ZONE_MAP.put("KOHIMA", zoneV);
        TOWN_ZONE_MAP.put("AIZAWL", zoneV);
        TOWN_ZONE_MAP.put("SHILLONG", zoneV);
        TOWN_ZONE_MAP.put("ITANAGAR", zoneV);
        TOWN_ZONE_MAP.put("GANGTOK", zoneV);
        TOWN_ZONE_MAP.put("PORT BLAIR", zoneV);
        TOWN_ZONE_MAP.put("SRINAGAR", zoneV);
        TOWN_ZONE_MAP.put("KUPWARA", zoneV);
        TOWN_ZONE_MAP.put("BARAMULLA", zoneV);
        TOWN_ZONE_MAP.put("JAMMU", zoneV);
        TOWN_ZONE_MAP.put("CHAMOLI", zoneV);
        TOWN_ZONE_MAP.put("PITHORAGARH", zoneV);
        TOWN_ZONE_MAP.put("BAGESHWAR", zoneV);
        TOWN_ZONE_MAP.put("UTTARKASHI", zoneV);

        // --- ZONE IV (High Risk - Z = 0.24g) ---
        String zoneIV = "Zone IV (High Risk - Z = 0.24g)";
        TOWN_ZONE_MAP.put("DELHI", zoneIV);
        TOWN_ZONE_MAP.put("NEW DELHI", zoneIV);
        TOWN_ZONE_MAP.put("ROORKEE", zoneIV);
        TOWN_ZONE_MAP.put("HARIDWAR", zoneIV);
        TOWN_ZONE_MAP.put("DEHRADUN", zoneIV);
        TOWN_ZONE_MAP.put("HALDWANI", zoneIV);
        TOWN_ZONE_MAP.put("NAINITAL", zoneIV);
        TOWN_ZONE_MAP.put("SHIMLA", zoneIV);
        TOWN_ZONE_MAP.put("MANALI", zoneIV);
        TOWN_ZONE_MAP.put("DHARAMSHALA", zoneIV);
        TOWN_ZONE_MAP.put("KANGRA", zoneIV);
        TOWN_ZONE_MAP.put("PATNA", zoneIV);
        TOWN_ZONE_MAP.put("DARBHANGA", zoneIV);
        TOWN_ZONE_MAP.put("MADHUBANI", zoneIV);
        TOWN_ZONE_MAP.put("SITAMARHI", zoneIV);
        TOWN_ZONE_MAP.put("PURNEA", zoneIV);
        TOWN_ZONE_MAP.put("MEERUT", zoneIV);
        TOWN_ZONE_MAP.put("GHAZIABAD", zoneIV);
        TOWN_ZONE_MAP.put("NOIDA", zoneIV);
        TOWN_ZONE_MAP.put("GREATER NOIDA", zoneIV);
        TOWN_ZONE_MAP.put("GAUTAM BUDDH NAGAR", zoneIV);
        TOWN_ZONE_MAP.put("MORADABAD", zoneIV);
        TOWN_ZONE_MAP.put("AMRITSAR", zoneIV);
        TOWN_ZONE_MAP.put("JALANDHAR", zoneIV);
        TOWN_ZONE_MAP.put("LUDHIANA", zoneIV);

        // --- ZONE III (Moderate Risk - Z = 0.16g) ---
        String zoneIII = "Zone III (Moderate Risk - Z = 0.16g)";
        TOWN_ZONE_MAP.put("MUMBAI", zoneIII);
        TOWN_ZONE_MAP.put("PUNE", zoneIII);
        TOWN_ZONE_MAP.put("THANE", zoneIII);
        TOWN_ZONE_MAP.put("NASHIK", zoneIII);
        TOWN_ZONE_MAP.put("SURAT", zoneIII);
        TOWN_ZONE_MAP.put("AHMEDABAD", zoneIII);
        TOWN_ZONE_MAP.put("VADODARA", zoneIII);
        TOWN_ZONE_MAP.put("RAJKOT", zoneIII);
        TOWN_ZONE_MAP.put("BHAVNAGAR", zoneIII);
        TOWN_ZONE_MAP.put("KOLKATA", zoneIII);
        TOWN_ZONE_MAP.put("ASANSOL", zoneIII);
        TOWN_ZONE_MAP.put("SILIGURI", zoneIII);
        TOWN_ZONE_MAP.put("BHUBANESWAR", zoneIII);
        TOWN_ZONE_MAP.put("CUTTACK", zoneIII);
        TOWN_ZONE_MAP.put("ROURKELA", zoneIII);
        TOWN_ZONE_MAP.put("KOCHI", zoneIII);
        TOWN_ZONE_MAP.put("THIRUVANANTHAPURAM", zoneIII);
        TOWN_ZONE_MAP.put("KOZHIKODE", zoneIII);
        TOWN_ZONE_MAP.put("CHENNAI", zoneIII);
        TOWN_ZONE_MAP.put("COIMBATORE", zoneIII);
        TOWN_ZONE_MAP.put("AGRA", zoneIII);
        TOWN_ZONE_MAP.put("KANPUR", zoneIII);
        TOWN_ZONE_MAP.put("LUCKNOW", zoneIII);
        TOWN_ZONE_MAP.put("VARANASI", zoneIII);
        TOWN_ZONE_MAP.put("PRAYAGRAJ", zoneIII);

        // --- ZONE II (Low Risk - Z = 0.10g) ---
        String zoneII = "Zone II (Low Risk - Z = 0.10g)";
        TOWN_ZONE_MAP.put("HYDERABAD", zoneII);
        TOWN_ZONE_MAP.put("BENGALURU", zoneII);
        TOWN_ZONE_MAP.put("MYSURU", zoneII);
        TOWN_ZONE_MAP.put("JAIPUR", zoneII);
        TOWN_ZONE_MAP.put("JODHPUR", zoneII);
        TOWN_ZONE_MAP.put("UDAIPUR", zoneII);
        TOWN_ZONE_MAP.put("BHOPAL", zoneII);
        TOWN_ZONE_MAP.put("INDORE", zoneII);
        TOWN_ZONE_MAP.put("GWALIOR", zoneII);
        TOWN_ZONE_MAP.put("NAGPUR", zoneII);
        TOWN_ZONE_MAP.put("VISAKHAPATNAM", zoneII);
        TOWN_ZONE_MAP.put("VIJAYAWADA", zoneII);
    }

    public SeismicData getSeismicData(String pincode) {
        try {
            String response = restTemplate.getForObject(POSTAL_API_URL + pincode, String.class);
            JsonNode root = objectMapper.readTree(response);

            if (root == null || !root.isArray() || root.get(0) == null) {
                return null;
            }

            JsonNode firstResponse = root.get(0);
            String status = firstResponse.path("Status").asText();
            if (!"Success".equalsIgnoreCase(status)) {
                return null;
            }

            JsonNode postOfficeArray = firstResponse.path("PostOffice");
            if (!postOfficeArray.isArray() || postOfficeArray.size() == 0) {
                return null;
            }

            JsonNode geoNode = postOfficeArray.get(0);
            String postOfficeName = geoNode.path("Name").asText("");
            String district = geoNode.path("District").asText("");
            String state = geoNode.path("State").asText("");
            String locationName = postOfficeName + ", " + district;

            // Execute search cascade hierarchy
            String seismicZone = evaluateSeismicZoneCascade(postOfficeName, district, state);
            String soilType = determineSoilType(state);
            String terrain = determineTerrain(state);
            String riskSummary = generateRiskSummary(locationName, state, seismicZone, soilType);

            return new SeismicData(
                    pincode,
                    state,
                    locationName,
                    seismicZone,
                    soilType,
                    terrain,
                    riskSummary
            );

        } catch (Exception e) {
            // Safe production logging fallback - returns null smoothly to let controller handle UI feedback
            return null;
        }
    }

    private String evaluateSeismicZoneCascade(String townName, String district, String state) {
        // Safe string conversions ensuring no null pointer exceptions can fire
        String cleanTown = townName != null ? townName.trim().toUpperCase() : "";
        String cleanDistrict = district != null ? district.trim().toUpperCase() : "";

        // 1. Direct municipal key match lookup
        if (!cleanTown.isEmpty() && TOWN_ZONE_MAP.containsKey(cleanTown)) {
            return TOWN_ZONE_MAP.get(cleanTown);
        }

        // 2. District level resolution fallback check
        if (!cleanDistrict.isEmpty() && TOWN_ZONE_MAP.containsKey(cleanDistrict)) {
            return TOWN_ZONE_MAP.get(cleanDistrict);
        }

        // 3. Macro state scale baseline fallback
        return fallbackStateLevelZone(state);
    }

    private String fallbackStateLevelZone(String state) {
        if (state == null) return "Zone II (Low Risk - Z = 0.10g)";
        String cleanState = state.trim().toUpperCase();

        if (cleanState.matches("ASSAM|MEGHALAYA|MANIPUR|MIZORAM|NAGALAND|TRIPURA|ANDAMAN AND NICOBAR ISLANDS")) {
            return "Zone V (Very High Risk - Z = 0.36g)";
        }
        if (cleanState.matches("UTTARAKHAND|HIMACHAL PRADESH|DELHI|JAMMU AND KASHMIR|LADAKH|SIKKIM")) {
            return "Zone IV (High Risk - Z = 0.24g)";
        }
        if (cleanState.matches("MAHARASHTRA|GUJARAT|WEST BENGAL|ODISHA|KERALA|GOA|TAMIL NADU|BIHAR|UTTAR PRADESH|PUNJAB|HARYANA")) {
            return "Zone III (Moderate Risk - Z = 0.16g)";
        }
        return "Zone II (Low Risk - Z = 0.10g)";
    }

    private String determineSoilType(String state) {
        if (state == null) return "Type I – Hard Rock / Basaltic Deccan Bedrock";
        String cleanState = state.trim().toUpperCase();

        if (cleanState.matches("WEST BENGAL|ODISHA|ANDHRA PRADESH|TAMIL NADU")) {
            return "Type III – Soft Alluvial / Deep River Deltaic Silt";
        }
        if (cleanState.matches("UTTAR PRADESH|BIHAR|DELHI|PUNJAB|HARYANA|ASSAM|UTTARAKHAND")) {
            return "Type II – Medium Dense Alluvium / Stiff Clay Plains";
        }
        return "Type I – Hard Rock / Basaltic Deccan Bedrock";
    }

    private String determineTerrain(String state) {
        if (state == null) return "Low-Lying Flat Coastal/Alluvial Plain";
        String cleanState = state.trim().toUpperCase();

        if (cleanState.matches("UTTARAKHAND|HIMACHAL PRADESH|JAMMU AND KASHMIR|SIKKIM|ARUNACHAL PRADESH|MEGHALAYA|NAGALAND|MANIPUR|MIZORAM")) {
            return "Hilly / High-Slope Mountainous Terrain";
        }
        if (cleanState.matches("MAHARASHTRA|MADHYA PRADESH|KARNATAKA|TELANGANA")) {
            return "Stable Elevated Deccan Plateau Plain";
        }
        return "Low-Lying Flat Coastal/Alluvial Plain";
    }

    private String generateRiskSummary(String location, String state, String zone, String soil) {
        String shortZone = zone.split("\\(")[0].trim();
        return String.format(
                "Automated geotechnical profile compiled for %s, %s. Local classification falls into %s under IS 1893:2016 parameters. " +
                        "The regional subsurface geological structure features %s. Foundation profiles must strictly adhere to municipal code " +
                        "regulations and structural design validations matching these specific environmental constraints.",
                location, state, shortZone, soil.toLowerCase()
        );
    }
    /**
     * Production Controller Alias Bridge Method
     * Redirects existing PortalController calls smoothly to the new API engine
     */
    public SeismicData getSafetyProfile(String pincode) {
        return this.getSeismicData(pincode);
    }
}