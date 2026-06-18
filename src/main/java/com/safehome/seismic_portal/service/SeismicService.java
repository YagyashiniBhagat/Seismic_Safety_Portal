package com.safehome.seismic_portal.service;

import com.safehome.seismic_portal.model.SeismicData;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SeismicService {
    private final Map<String, SeismicData> database = new HashMap<>();

    public SeismicService() {
        // Pre-populating our prototype with valid IS 1893 Uttarakhand parameters
        database.put("247667", new SeismicData(
                "247667", "Haridwar", "Roorkee", "Zone IV (High Risk)",
                "Type II (Medium Alluvial/Silt)", "Plain Flat Ground",
                "Roorkee is located in a high-risk seismic zone. While flat ground reduces slope failure risks, the local Type II soil tends to amplify seismic shockwaves. Ensure strict structural compliance before purchasing multi-story apartments."
        ));

        database.put("248001", new SeismicData(
                "248001", "Dehradun", "Dehradun City", "Zone IV (High Risk)",
                "Type I (Rock/Hard Soil)", "Hilly/Himalayan Slope",
                "Dehradun sits in a high damage hazard zone. The rocky terrain provides good foundation support, but mountain slopes present structural risks like landslide vulnerability during high-intensity tremors."
        ));
    }

    public SeismicData getSafetyProfile(String pincode) {
        return database.get(pincode);
    }
}
