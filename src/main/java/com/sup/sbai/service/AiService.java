package com.sup.sbai.service;

import com.example.openai.dto.ChatRequest;
import com.example.openai.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final WebClient webClient;
    private final String deploymentId;
    private final String apiVersion;

    public AiService(
            @Value("${azure.openai.endpoint}") String endpoint,
            @Value("${azure.openai.api-key}") String apiKey,
            @Value("${azure.openai.deployment-id}") String deploymentId,
            @Value("${azure.openai.api-version}") String apiVersion) {

        this.webClient = WebClient.builder()
                .baseUrl(endpoint)
                .defaultHeader("api-key", apiKey)
                .build();
        this.deploymentId = deploymentId;
        this.apiVersion = apiVersion;
    }

    public String chat(String userPrompt) {
        ChatRequest request = new ChatRequest(
                List.of(Map.of("role", "user", "content", userPrompt))
        );

        ChatResponse response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/openai/deployments/" + deploymentId + "/chat/completions")
                        .queryParam("api-version", apiVersion)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        if (response != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        }
        return "No response from Azure OpenAI";
    }
}
