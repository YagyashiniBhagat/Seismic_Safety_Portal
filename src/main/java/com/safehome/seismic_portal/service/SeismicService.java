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
                "Type II – Medium Alluvial/Silt", "Plain Flat Ground",
                ""
        ));
        database.put("248001", new SeismicData(
                "248001", "Dehradun", "Dehradun City", "Zone IV (High Risk)",
                "Type I – Rock / Hard Soil", "Hilly / Himalayan Slope",
                ""
        ));
        database.put("110001", new SeismicData(
                "110001", "Central Delhi", "New Delhi", "Zone IV (High Risk)",
                "Type II – Alluvial Plains", "Flat Urban Plain",
                ""
        ));
        database.put("400001", new SeismicData(
                "400001", "Mumbai City", "Mumbai (Fort Area)", "Zone III (Moderate Risk)",
                "Type I – Hard Rock / Basalt", "Coastal Rocky Terrain",
                ""
        ));
        database.put("700001", new SeismicData(
                "700001", "Kolkata", "Kolkata (BBD Bagh)", "Zone III (Moderate Risk)",
                "Type III – Soft Alluvial / River Delta", "Low-lying Delta Plain",
                ""
        ));
        database.put("500001", new SeismicData(
                "500001", "Hyderabad", "Hyderabad (Old City)", "Zone II (Low Risk)",
                "Type I – Granite / Hard Rock", "Deccan Plateau",
                ""
        ));
        database.put("395001", new SeismicData(
                "395001", "Surat", "Surat City", "Zone III (Moderate Risk)",
                "Type II – Coastal Alluvial", "Low Coastal Plain",
                ""
        ));
    }

    public SeismicData getSafetyProfile(String pincode) {
        return database.get(pincode);
    }
}
