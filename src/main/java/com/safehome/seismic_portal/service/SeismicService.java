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
                "Roorkee is in a high-risk seismic zone. Local Type II soil amplifies shockwaves significantly. Strict structural compliance is essential before purchasing multi-story apartments."
        ));
        database.put("248001", new SeismicData(
                "248001", "Dehradun", "Dehradun City", "Zone IV (High Risk)",
                "Type I – Rock / Hard Soil", "Hilly / Himalayan Slope",
                "Dehradun sits in a high damage hazard zone. Rocky terrain provides good foundation support, but mountain slopes present landslide vulnerability during high-intensity tremors."
        ));
        database.put("110001", new SeismicData(
                "110001", "Central Delhi", "New Delhi", "Zone IV (High Risk)",
                "Type II – Alluvial Plains", "Flat Urban Plain",
                "New Delhi lies in seismic Zone IV with dense alluvial soil. Older construction in central Delhi is particularly vulnerable to liquefaction and shear failure. Verify structural audits carefully."
        ));
        database.put("400001", new SeismicData(
                "400001", "Mumbai City", "Mumbai (Fort Area)", "Zone III (Moderate Risk)",
                "Type I – Hard Rock / Basalt", "Coastal Rocky Terrain",
                "Mumbai's basaltic rock foundation provides naturally good seismic resistance. Zone III designation means moderate risk — modern buildings are generally safe, but older unreinforced masonry structures need inspection."
        ));
        database.put("700001", new SeismicData(
                "700001", "Kolkata", "Kolkata (BBD Bagh)", "Zone III (Moderate Risk)",
                "Type III – Soft Alluvial / River Delta", "Low-lying Delta Plain",
                "Kolkata's Gangetic delta soil is extremely soft and prone to high amplification of seismic waves. Even moderate Zone III earthquakes can cause disproportionate structural damage. Prioritise pile foundation verification."
        ));
        database.put("500001", new SeismicData(
                "500001", "Hyderabad", "Hyderabad (Old City)", "Zone II (Low Risk)",
                "Type I – Granite / Hard Rock", "Deccan Plateau",
                "Hyderabad rests on the stable Deccan Plateau with hard granite bedrock — among the lowest seismic risk zones in India. Modern construction here faces minimal seismic threat."
        ));
        database.put("395001", new SeismicData(
                "395001", "Surat", "Surat City", "Zone III (Moderate Risk)",
                "Type II – Coastal Alluvial", "Low Coastal Plain",
                "Surat's coastal alluvial soil in Zone III requires careful foundation design. Proximity to the historically active Kutch fault zone warrants seismic compliance checks for any high-rise construction."
        ));
    }

    public SeismicData getSafetyProfile(String pincode) {
        return database.get(pincode);
    }
}
