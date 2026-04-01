package tn.esprit.msrecruitmentservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

/**
 * Service IA — Génération de description de poste via Hugging Face Router.
 *
 * Modèle : meta-llama/Llama-3.1-8B-Instruct  (gratuit, multilingue, performant)
 * URL    : https://router.huggingface.co/v1/chat/completions
 *
 * ⚠️  api-inference.huggingface.co → 410 Gone (dépréciée)
 * ⚠️  router.huggingface.co/hf-inference/models/... → 404 (mauvais chemin)
 * ✅  router.huggingface.co/v1/chat/completions → endpoint universel correct
 */
@Service
public class HuggingFaceService {

    // Modèle disponible gratuitement via le router HF
    // Alternatives : "google/gemma-2-2b-it" | "Qwen/Qwen2.5-7B-Instruct"
    private static final String MODEL_ID = "meta-llama/Llama-3.1-8B-Instruct";

    // ✅ URL correcte — router universel HF (compatible OpenAI)
    private static final String HF_API_URL = "https://router.huggingface.co/v1/chat/completions";

    @Value("${huggingface.api.token}")
    private String apiToken;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Génère automatiquement une description de poste professionnelle.
     */
    public String generateJobDescription(String titre, String skills,
                                         String typeContrat, String localisation) {
        try {
            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "model", MODEL_ID,
                    "messages", List.of(
                            Map.of("role", "system", "content",
                                    "Tu es un expert RH spécialisé dans la rédaction d'offres d'emploi en Tunisie. " +
                                            "Réponds toujours en français. Sois professionnel et concis."),
                            Map.of("role", "user", "content", buildPrompt(titre, skills, typeContrat, localisation))
                    ),
                    "max_tokens", 500,
                    "temperature", 0.7
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(HF_API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiToken)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseResponse(response.body());
            } else {
                return "Erreur API Hugging Face [" + response.statusCode() + "]: " + response.body();
            }

        } catch (Exception e) {
            return "Erreur lors de la génération IA : " + e.getMessage();
        }
    }

    private String buildPrompt(String titre, String skills, String typeContrat, String localisation) {
        return String.format(
                "Génère une description de poste complète pour :\n" +
                        "- Titre : %s\n" +
                        "- Contrat : %s\n" +
                        "- Localisation : %s\n" +
                        "- Compétences : %s\n\n" +
                        "Inclure : présentation du poste, missions (4-5 points), profil recherché, avantages. " +
                        "Réponds directement sans introduction.",
                titre, typeContrat, localisation, skills
        );
    }

    /**
     * Parse la réponse OpenAI-compatible.
     * Format : { "choices": [ { "message": { "content": "..." } } ] }
     */
    private String parseResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode choices = root.get("choices");
            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode content = choices.get(0).path("message").path("content");
                if (!content.isMissingNode()) {
                    return content.asText().trim();
                }
            }
            return "Réponse inattendue : " + responseBody;
        } catch (Exception e) {
            return "Erreur parsing : " + e.getMessage();
        }
    }
}