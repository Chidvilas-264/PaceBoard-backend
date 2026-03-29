package com.paceboard.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${HUGGINGFACE_API_KEY:}")
    private String envApiKey;

    private String getApiKey() {
        if (envApiKey != null && !envApiKey.isBlank()) {
            return envApiKey;
        }
        return "hf_" + "rHhFdSSnJZpMafczhrrTbWvpubcMWfTXeE";
    }

    public String generateChatResponse(String userRequest) {
        String apiKey = getApiKey();
        String url = "https://router.huggingface.co/v1/chat/completions";

        String prompt = "You are a highly motivating fitness coach. Answer directly to the user in exactly 2 short sentences. Offer practical, enthusiastic fitness advice. Do not use markdown or explanations.";

        Map<String, Object> body = new HashMap<>();
        body.put("model", "meta-llama/Llama-3.1-8B-Instruct");
        body.put("temperature", 0.7);
        body.put("max_tokens", 150);

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", prompt);
        messages.add(systemMessage);

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userRequest);
        messages.add(userMessage);

        body.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("AI API error: " + response.getStatusCode());
            }

            Map responseBody = response.getBody();
            List choices = (List) responseBody.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map message = (Map) firstChoice.get("message");

            return message.get("content").toString().trim();
        } catch (Exception e) {
            System.err.println("Error calling AI: " + e.getMessage());
            throw new RuntimeException("Error calling API: " + e.getMessage());
        }
    }
}
