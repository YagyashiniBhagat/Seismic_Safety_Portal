package com.safehome.seismic_portal.model;

public class SeismicData {
    private String pincode;
    private String district;
    private String locationName;
    private String seismicZone;
    private String soilType;
    private String terrain;
    private String aiSummary;
    private String pgaValue;
    private String intensityLevel;
    private String structuralForecast;

    // Day 4 Addition: Geocoding Coordinates (Defaults set to Roorkee region)
    private double latitude = 29.8543;
    private double longitude = 77.8880;

    // Production-ready Constructor (Leaves the original 7-string footprint completely untouched!)
    public SeismicData(String pincode, String district, String locationName, String seismicZone, String soilType, String terrain, String aiSummary) {
        this.pincode = pincode;
        this.district = district;
        this.locationName = locationName;
        this.seismicZone = seismicZone;
        this.soilType = soilType;
        this.terrain = terrain;
        this.aiSummary = aiSummary;
    }

    // Getters (Required by Thymeleaf to read data onto the screen)
    public String getPincode() { return pincode; }
    public String getDistrict() { return district; }
    public String getLocationName() { return locationName; }
    public String getSeismicZone() { return seismicZone; }
    public String getSoilType() { return soilType; }
    public String getTerrain() { return terrain; }
    public String getAiSummary() { return aiSummary; }
    public String getPgaValue() { return pgaValue; }
    public void setPgaValue(String pgaValue) { this.pgaValue = pgaValue; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public String getIntensityLevel() { return intensityLevel; }
    public void setIntensityLevel(String intensityLevel) { this.intensityLevel = intensityLevel; }

    public String getStructuralForecast() { return structuralForecast; }
    public void setStructuralForecast(String structuralForecast) { this.structuralForecast = structuralForecast; }

    // New Coordinates Getters & Setters
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}