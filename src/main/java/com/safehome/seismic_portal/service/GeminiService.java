package com.safehome.seismic_portal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final RestClient restClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiService() {
        this.restClient = RestClient.builder().build();
    }

    public String generateRiskSummary(String location, String zone, String soilType,
                                      String terrain, String constructionEra) {
        String prompt = "You are a professional earthquake risk analyst. " +
                "Write a 2-3 sentence plain-language seismic risk summary for a homebuyer " +
                "evaluating a property in " + location + ", India. " +
                "The property is in " + zone + ", soil type is " + soilType + ", " +
                "terrain is " + terrain + ", and the building construction era is " + constructionEra + ". " +
                "Be specific, factual, and reference Indian Standard codes where relevant. " +
                "Do not use bullet points. Write in plain flowing sentences.";

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        String endpointUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        try {
            Map<String, Object> response = restClient.post()
                    .uri(endpointUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            List<?> candidates = (List<?>) response.get("candidates");
            Map<?, ?> firstCandidate = (Map<?, ?>) candidates.get(0);
            Map<?, ?> content = (Map<?, ?>) firstCandidate.get("content");
            List<?> parts = (List<?>) content.get("parts");
            Map<?, ?> firstPart = (Map<?, ?>) parts.get(0);
            return (String) firstPart.get("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "AI summary temporarily unavailable. Based on available data, this location falls in " +
                    zone + " with " + soilType + " soil conditions.";
        }
    }
}