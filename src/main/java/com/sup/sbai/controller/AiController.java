package com.sup.sbai.controller;

import com.sup.sbai.service.AiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AiController {

    private final AiService openAIService;

    public AiController(AiService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String prompt) {
        return openAIService.chat(prompt);
    }
}
