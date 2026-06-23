package com.safehome.seismic_portal.service;

import com.safehome.seismic_portal.model.SeismicData;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SeismicService {
    private final Map<String, SeismicData> database = new HashMap<>();

    public SeismicService() {
        SeismicData roorkee = new SeismicData("247667", "Haridwar", "Roorkee", "Zone IV (High Risk)", "Type II – Medium Alluvial/Silt", "Plain Flat Ground", "");
        roorkee.setLatitude(29.8543);
        roorkee.setLongitude(77.8880);
        database.put("247667", roorkee);

        SeismicData dehradun = new SeismicData("248001", "Dehradun", "Dehradun City", "Zone IV (High Risk)", "Type I – Rock / Hard Soil", "Hilly / Himalayan Slope", "");
        dehradun.setLatitude(30.3165);
        dehradun.setLongitude(78.0322);
        database.put("248001", dehradun);

        SeismicData delhi = new SeismicData("110001", "Central Delhi", "New Delhi", "Zone IV (High Risk)", "Type II – Alluvial Plains", "Flat Urban Plain", "");
        delhi.setLatitude(28.6139);
        delhi.setLongitude(77.2090);
        database.put("110001", delhi);

        SeismicData mumbai = new SeismicData("400001", "Mumbai City", "Mumbai (Fort Area)", "Zone III (Moderate Risk)", "Type I – Hard Rock / Basalt", "Coastal Rocky Terrain", "");
        mumbai.setLatitude(18.9388);
        mumbai.setLongitude(72.8354);
        database.put("400001", mumbai);

        SeismicData kolkata = new SeismicData("700001", "Kolkata", "Kolkata (BBD Bagh)", "Zone III (Moderate Risk)", "Type III – Soft Alluvial / River Delta", "Low-lying Delta Plain", "");
        kolkata.setLatitude(22.5726);
        kolkata.setLongitude(88.3639);
        database.put("700001", kolkata);

        SeismicData hyderabad = new SeismicData("500001", "Hyderabad", "Hyderabad (Old City)", "Zone II (Low Risk)", "Type I – Granite / Hard Rock", "Deccan Plateau", "");
        hyderabad.setLatitude(17.3616);
        hyderabad.setLongitude(78.4747);
        database.put("500001", hyderabad);

        SeismicData surat = new SeismicData("395001", "Surat", "Surat City", "Zone III (Moderate Risk)", "Type II – Coastal Alluvial", "Low Coastal Plain", "");
        surat.setLatitude(21.1702);
        surat.setLongitude(72.8311);
        database.put("395001", surat);
    }

    public SeismicData getSafetyProfile(String pincode) {
        return database.get(pincode);
    }
}
