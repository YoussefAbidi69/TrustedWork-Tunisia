package tn.esprit.reviewservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.reviewservice.dto.ai.AdminInsightDTO;
import tn.esprit.reviewservice.dto.ai.AiModerationResult;
import tn.esprit.reviewservice.dto.ai.ReviewSummaryDTO;
import tn.esprit.reviewservice.entity.enums.SentimentLabel;
import tn.esprit.reviewservice.service.interfaces.IGeminiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GeminiServiceImpl implements IGeminiService {

    @Value("${huggingface.api.token}")
    private String huggingFaceToken;

    @Value("${huggingface.api.url}")
    private String huggingFaceUrl;

    @Value("${huggingface.api.model}")
    private String huggingFaceModel;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AiModerationResult analyzeReview(String comment, Double ratingAverage) {
        try {
            String prompt = """
                    You are an AI moderation engine for a professional freelance review platform.

                    Return STRICT JSON ONLY.
                    No markdown. No explanation.

                    Expected JSON format:
                    {
                      "sentimentLabel": "POSITIVE|NEUTRAL|NEGATIVE|TOXIC",
                      "riskScore": 0,
                      "flagged": false,
                      "shortSummary": "short summary"
                    }

                    Rules:
                    - POSITIVE: clearly favorable feedback
                    - NEUTRAL: mixed or moderate feedback
                    - NEGATIVE: dissatisfaction or criticism
                    - TOXIC: insults, hate, abuse, threats, defamation, scam-like tone
                    - riskScore must be from 0 to 100
                    - flagged must be true if suspicious, toxic, abusive, scammy, or highly risky

                    Rating average: %s
                    Review comment: %s
                    """.formatted(ratingAverage, comment);

            String rawText = callGemini(prompt);
            return parseModerationJson(rawText);

        } catch (Exception e) {
            log.error("Hugging Face analyzeReview error: {}", e.getMessage(), e);
            return fallback(comment, ratingAverage);
        }
    }

    @Override
    public ReviewSummaryDTO summarizeUserReviews(String reviewsText) {
        try {
            String prompt = """
Return ONLY valid JSON.
No markdown. No explanation.
Use double quotes only.

{
  "overall": "",
  "strengths": "",
  "weaknesses": ""
}

Reviews:
%s
""".formatted(reviewsText);

            log.info("Sending summary request to Hugging Face...");
            String rawText = callGemini(prompt);
            log.info("Hugging Face raw summary response: {}", rawText);

            return parseSummary(rawText);

        } catch (Exception e) {
            log.error("HUGGING FACE SUMMARY ERROR FULL STACK", e);

            ReviewSummaryDTO fallback = new ReviewSummaryDTO();
            fallback.setOverall("Unavailable");
            fallback.setStrengths("Unavailable");
            fallback.setWeaknesses("Unavailable");

            return fallback;
        }
    }

    private ReviewSummaryDTO parseSummary(String rawText) {
        try {
            String cleaned = sanitizeAiJson(rawText);
            JsonNode json = objectMapper.readTree(cleaned);
            String weaknesses = json.path("weaknesses").asText();


            ReviewSummaryDTO dto = new ReviewSummaryDTO();
            dto.setOverall(json.path("overall").asText("N/A"));
            dto.setStrengths(json.path("strengths").asText("N/A"));
            dto.setWeaknesses(
                    (weaknesses == null || weaknesses.isBlank())
                            ? "None identified"
                            : weaknesses);

            return dto;

        } catch (Exception e) {
            log.warn("Summary parsing failed. Raw={}", rawText);

            ReviewSummaryDTO fallback = new ReviewSummaryDTO();
            fallback.setOverall("Unavailable");
            fallback.setStrengths("Unavailable");
            fallback.setWeaknesses("Unavailable");

            return fallback;
        }
    }

    @Override
    public AdminInsightDTO generateAdminInsight(String reviewComment, String reclamationReason) {
        try {
            String prompt = """
Return ONLY valid JSON.
Use double quotes only.
Do not use markdown.
Do not add explanations.
Make sure the JSON is complete and closed properly.

Expected JSON format:
{
  "riskLevel": "HIGH|MEDIUM|LOW",
  "issue": "short issue description",
  "actions": ["action1", "action2", "action3"]
}

Review:
%s

Reclamation reason:
%s
""".formatted(reviewComment, reclamationReason);

            String rawText = callGemini(prompt);
            return parseAdminInsight(rawText);

        } catch (Exception e) {
            log.error("Hugging Face generateAdminInsight error: {}", e.getMessage(), e);

            AdminInsightDTO fallback = new AdminInsightDTO();
            fallback.setRiskLevel("UNKNOWN");
            fallback.setIssue("Insight admin IA indisponible.");
            fallback.setActions(List.of("Manual review required"));
            return fallback;
        }
    }

    private AdminInsightDTO parseAdminInsight(String rawText) {
        try {
            String cleaned = sanitizeAiJson(rawText);
            JsonNode json = objectMapper.readTree(cleaned);

            AdminInsightDTO dto = new AdminInsightDTO();
            dto.setRiskLevel(json.path("riskLevel").asText("UNKNOWN"));
            dto.setIssue(json.path("issue").asText("N/A"));

            List<String> actions = new ArrayList<>();
            JsonNode actionsNode = json.path("actions");

            if (actionsNode.isArray()) {
                for (JsonNode node : actionsNode) {
                    actions.add(node.asText());
                }
            }

            if (actions.isEmpty()) {
                actions.add("Manual review required");
            }

            dto.setActions(actions);
            return dto;

        } catch (Exception e) {
            log.warn("Admin insight parsing failed, fallback used. Raw={}", rawText);

            AdminInsightDTO fallback = new AdminInsightDTO();
            fallback.setRiskLevel("UNKNOWN");
            fallback.setIssue("Parsing failed");
            fallback.setActions(List.of("Manual review required"));
            return fallback;
        }
    }

    private String sanitizeAiJson(String text) {
        if (text == null || text.isBlank()) {
            return "{}";
        }

        String cleaned = text.trim();

        cleaned = cleaned.replace("```json", "")
                .replace("```", "")
                .trim();

        // Si le modèle renvoie du pseudo-JSON avec quotes simples
        cleaned = cleaned.replace("'", "\"");

        // Si l'objet commence par { mais ne finit pas par }
        if (cleaned.startsWith("{") && !cleaned.endsWith("}")) {
            cleaned = cleaned + "}";
        }

        return cleaned;
    }

    private String callGemini(String prompt) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", huggingFaceModel);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        requestBody.put("messages", List.of(message));
        requestBody.put("temperature", 0.3);
        requestBody.put("max_tokens", 300);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(huggingFaceToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                huggingFaceUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        log.info("Hugging Face HTTP status: {}", response.getStatusCode());
        log.info("Hugging Face HTTP body: {}", response.getBody());

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Réponse Hugging Face invalide");
        }

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode contentNode = root.path("choices").path(0).path("message").path("content");

        if (!contentNode.isMissingNode() && !contentNode.asText().isBlank()) {
            return contentNode.asText();
        }

        if (root.has("error")) {
            throw new RuntimeException("Erreur Hugging Face: " + root.path("error"));
        }

        throw new RuntimeException("Texte Hugging Face introuvable dans la réponse");
    }

    private AiModerationResult parseModerationJson(String rawText) {
        try {
            String cleaned = cleanJson(rawText);
            JsonNode json = objectMapper.readTree(cleaned);

            String sentimentValue = json.path("sentimentLabel").asText("NEUTRAL");
            double riskScore = json.path("riskScore").asDouble(0.0);
            boolean flagged = json.path("flagged").asBoolean(false);
            String shortSummary = json.path("shortSummary").asText("Analyse IA effectuée");

            SentimentLabel sentimentLabel;
            try {
                sentimentLabel = SentimentLabel.valueOf(sentimentValue.toUpperCase());
            } catch (Exception e) {
                sentimentLabel = SentimentLabel.NEUTRAL;
            }

            if (riskScore < 0) riskScore = 0;
            if (riskScore > 100) riskScore = 100;

            return AiModerationResult.builder()
                    .sentimentLabel(sentimentLabel)
                    .riskScore(riskScore)
                    .flagged(flagged)
                    .shortSummary(shortSummary)
                    .rawResponse(cleaned)
                    .build();

        } catch (Exception e) {
            log.warn("Parsing AI JSON failed, fallback used. Raw={}", rawText);

            SentimentLabel sentiment =
                    rawText != null && rawText.toUpperCase().contains("TOXIC") ? SentimentLabel.TOXIC :
                            rawText != null && rawText.toUpperCase().contains("NEGATIVE") ? SentimentLabel.NEGATIVE :
                                    rawText != null && rawText.toUpperCase().contains("POSITIVE") ? SentimentLabel.POSITIVE :
                                            SentimentLabel.NEUTRAL;

            boolean flagged = rawText != null && rawText.toLowerCase().contains("true");

            return AiModerationResult.builder()
                    .sentimentLabel(sentiment)
                    .riskScore(50.0)
                    .flagged(flagged)
                    .shortSummary("Analyse IA partielle")
                    .rawResponse(rawText)
                    .build();
        }
    }

    private String cleanJson(String text) {
        if (text == null) return "{}";

        return text.replace("```json", "")
                .replace("```", "")
                .trim();
    }

    private String cleanText(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }

        return text.replace("```", "").trim();
    }

    private AiModerationResult fallback(String comment, Double ratingAverage) {
        double risk = 0.0;

        if (ratingAverage != null && ratingAverage <= 2.0) {
            risk += 40.0;
        }

        if (comment != null) {
            String lower = comment.toLowerCase();

            if (lower.contains("arnaque")
                    || lower.contains("fraude")
                    || lower.contains("fake")
                    || lower.contains("mensonge")
                    || lower.contains("scam")
                    || lower.contains("voleur")
                    || lower.contains("haine")
                    || lower.contains("hate")) {
                risk += 30.0;
            }

            if (comment.length() < 10) {
                risk += 10.0;
            }
        }

        SentimentLabel sentiment;
        if (ratingAverage == null) {
            sentiment = SentimentLabel.NEUTRAL;
        } else if (ratingAverage >= 4.0) {
            sentiment = SentimentLabel.POSITIVE;
        } else if (ratingAverage <= 2.0) {
            sentiment = SentimentLabel.NEGATIVE;
        } else {
            sentiment = SentimentLabel.NEUTRAL;
        }

        risk = Math.min(risk, 100.0);

        return AiModerationResult.builder()
                .sentimentLabel(sentiment)
                .riskScore(risk)
                .flagged(risk >= 60.0)
                .shortSummary("Fallback analysis used")
                .rawResponse("fallback")
                .build();
    }
}