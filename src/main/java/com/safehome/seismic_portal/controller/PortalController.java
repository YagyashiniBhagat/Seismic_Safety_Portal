package com.safehome.seismic_portal.controller;

import org.springframework.stereotype.Controller;
import com.safehome.seismic_portal.model.SeismicData;
import com.safehome.seismic_portal.service.SeismicService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PortalController {
    private final SeismicService seismicService;

    public PortalController(SeismicService seismicService) {
        this.seismicService = seismicService;
    }

    // Displays the initial blank homepage layout
    @GetMapping("/")
    public String showHomepage() {
        return "index";
    }

    // Handles the form submission when the user clicks "Evaluate Location Safety"
    @PostMapping("/evaluate")
    public String evaluateSafety(
            @RequestParam("pincode") String pincode,
            @RequestParam(value = "propertyType", defaultValue = "independent-house") String propertyType,
            @RequestParam(value = "constructionEra", defaultValue = "post-2016") String constructionEra,
            Model model) {

        if (!pincode.matches("^[0-9]{6}$")) {
            model.addAttribute("error", "Please enter a valid 6-digit pincode.");
            model.addAttribute("found", false);
            return "index";
        }

        SeismicData data = seismicService.getSafetyProfile(pincode);

        if (data != null) {
            // ---- Dynamic PGA Simulation Logic ----
            // 1. Calculate PGA value based on the Seismic Zone from your service data
            String zone = data.getSeismicZone() != null ? data.getSeismicZone().toUpperCase() : "";
            if (zone.contains("V")) {
                data.setPgaValue("0.36g");
                data.setIntensityLevel("IX — Ruinous");
            } else if (zone.contains("IV")) {
                data.setPgaValue("0.24g");
                data.setIntensityLevel("VIII — Severe");
            } else if (zone.contains("III")) {
                data.setPgaValue("0.16g");
                data.setIntensityLevel("VII — Strong");
            } else {
                data.setPgaValue("0.10g");
                data.setIntensityLevel("VI — Moderate");
            }

            // 2. Generate a structural forecast combining Building Type and Code Era
            if (constructionEra.equals("pre-1990") || constructionEra.equals("1990-2002")) {
                data.setStructuralForecast("High Vulnerability. Built under legacy code rules. Highly susceptible to severe beam-column joint distress and masonry shear cracks.");
            } else if (constructionEra.equals("2002-2016")) {
                data.setStructuralForecast("Moderate Safety. Matches intermediate revision standards. Non-structural masonry partition wall cracking is still probable.");
            } else {
                data.setStructuralForecast("High Code Compliance. Designed under modern strict IS 1893:2016 guidelines. Low risk of structural damage or progressive failure.");
            }

            model.addAttribute("report", data);
            model.addAttribute("found", true);
        } else {
            model.addAttribute("error", "Data not available for this pincode yet.");
            model.addAttribute("found", false);
        }
        return "index";
    }
}
