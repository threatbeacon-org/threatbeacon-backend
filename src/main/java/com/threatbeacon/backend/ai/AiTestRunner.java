package com.threatbeacon.backend.ai;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AiTestRunner {

    @Bean
    @Profile("!test") // Ensure this does not run during unit tests
    public CommandLineRunner testAi(OpenAiChatModel chatModel) { // <-- Inyectamos el bean correcto: OpenAiChatModel
        return args -> {
            try {
                System.out.println("==================================================");
                System.out.println("Testing Spring AI - OpenAI Integration...");
                String response = chatModel.call("Say 'Hello, World!' in a pirate voice.");
                System.out.println("OpenAI Response: " + response);
                System.out.println("Integration Test Successful!");
                System.out.println("==================================================");
            } catch (Exception e) {
                System.err.println("==================================================");
                System.err.println("!!! Spring AI - OpenAI Integration Test FAILED !!!");
                System.err.println("Error: " + e.getMessage());
                System.err.println("Please ensure the OPENAI_API_KEY environment variable is set correctly.");
                System.err.println("==================================================");
            }
        };
    }
}
