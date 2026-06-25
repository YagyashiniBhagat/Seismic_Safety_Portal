package com.safehome.seismic_portal.controller;

import org.springframework.stereotype.Controller;
import com.safehome.seismic_portal.model.SeismicData;
import com.safehome.seismic_portal.service.SeismicService;
import com.safehome.seismic_portal.service.GeminiService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PortalController {

    private final SeismicService seismicService;
    private final GeminiService geminiService;

    public PortalController(SeismicService seismicService, GeminiService geminiService) {
        this.seismicService = seismicService;
        this.geminiService = geminiService;
    }

    @GetMapping("/")
    public String showHomepage() {
        return "index";
    }

    @PostMapping("/evaluate")
    public String evaluateSafety(
            @RequestParam("pincode") String pincode,
            @RequestParam(value = "propertyType", defaultValue = "independent-house") String propertyType,
            @RequestParam(value = "constructionEra", defaultValue = "post-2016") String constructionEra,
            Model model) {

        // 1. Strict structural length sanitation format validation
        if (pincode == null || !pincode.trim().matches("^[0-9]{6}$")) {
            model.addAttribute("error", "Please enter a valid 6-digit pincode.");
            model.addAttribute("found", false);
            return "index";
        }

        pincode = pincode.trim();

        // 2. Fetch data directly from the newly upgraded live Post Office API endpoint
        SeismicData data = seismicService.getSafetyProfile(pincode);
        if (data != null) {
            // PGA + Intensity calculation logic based on the dynamic cascade zone result
            String zone = data.getSeismicZone() != null ? data.getSeismicZone().toUpperCase() : "";

            if (zone.contains("ZONE V") || zone.contains("ZONE 5")) {
                data.setPgaValue("0.36g");
                data.setIntensityLevel("IX — Ruinous");
            } else if (zone.contains("ZONE IV") || zone.contains("ZONE 4")) {
                data.setPgaValue("0.24g");
                data.setIntensityLevel("VIII — Severe");
            } else if (zone.contains("ZONE III") || zone.contains("ZONE 3")) {
                data.setPgaValue("0.16g");
                data.setIntensityLevel("VII — Strong");
            } else {
                data.setPgaValue("0.10g");
                data.setIntensityLevel("VI — Moderate");
            }

            // Standardize string casing checking to support frontend drop-down options smoothly
            String cleanEra = constructionEra != null ? constructionEra.trim().toLowerCase() : "after 2016";

            // Structural forecasting evaluation matrix
            if (cleanEra.contains("before 1990") || cleanEra.contains("pre")) {
                data.setStructuralForecast("High Vulnerability. Built under legacy code rules. Highly susceptible to severe beam-column joint distress and masonry shear cracks.");
            } else if (cleanEra.contains("1990") || cleanEra.contains("2002-2016")) {
                // Catches the "1990 - 2002" or intermediate options cleanly
                if (cleanEra.contains("2002")) {
                    data.setStructuralForecast("Moderate Safety. Matches intermediate revision standards. Non-structural masonry partition wall cracking is still probable.");
                } else {
                    data.setStructuralForecast("High Vulnerability. Built under legacy code rules. Highly susceptible to severe beam-column joint distress.");
                }
            } else {
                data.setStructuralForecast("High Code Compliance. Designed under modern strict IS 1893:2016 guidelines. Low risk of structural damage or progressive failure.");
            }

            // Dynamic Gemini AI summary engine pipeline trigger
            String aiSummary = geminiService.generateRiskSummary(
                    data.getLocationName(),
                    data.getSeismicZone(),
                    data.getSoilType(),
                    data.getTerrain(),
                    constructionEra
            );
            data.setAiSummary(aiSummary);

            model.addAttribute("report", data);
            model.addAttribute("found", true);
        } else {
            // If the live postal lookup failed or couldn't reach network services, pass a clean message banner
            model.addAttribute("found", false);
            model.addAttribute("error", "notfound");
            model.addAttribute("searchedPincode", pincode);
        }

        return "index";
    }
}