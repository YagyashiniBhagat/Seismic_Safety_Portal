package com.safehome.seismic_portal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    private final RestClient restClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public ChatRestController() {
        // Initializes a native Spring HTTP Client engine
        this.restClient = RestClient.builder().build();
    }

    @PostMapping("/ask")
    public Map<String, String> askAiAgent(@RequestBody Map<String, String> payload) {
        String userQuestion = payload.get("question");
        String currentZone = payload.get("zone");
        String constructionEra = payload.get("era");

        // Format a system context prompt for the model
        String contextPrompt = "You are 'Safe Home Assistant', a professional earthquake engineering AI agent. " +
                "The user is evaluating an asset in India located in " + (currentZone != null ? currentZone : "unknown zone") +
                ", structural construction era: " + (constructionEra != null ? constructionEra : "unknown era") + ".\n" +
                "Provide an accurate engineering response to the user's question matching Indian Standard building codes (IS 1893:2016). " +
                "Be direct and keep your response limited to 2 or 3 sentences maximum.\n\n" +
                "User Question: " + userQuestion;

        // Construct the standard JSON request payload expected by the Google Gemini API v1beta
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", contextPrompt)
                        ))
                )
        );

        String endpointUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
        try {
            // Standard POST without the Bearer header
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
            String aiReply = (String) firstPart.get("text");

            return Map.of("reply", aiReply);

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("reply", "Cloud Error: " + e.getMessage());
        }
    }
}