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
 * Service IA — HuggingFace Router
 * Fonctionnalités :
 *   1. Génération de description de poste (déjà implémentée)
 *   2. Génération des clauses juridiques du contrat de travail (NOUVEAU)
 *
 * Modèle : meta-llama/Llama-3.1-8B-Instruct
 * URL    : https://router.huggingface.co/v1/chat/completions
 */
@Service
public class HuggingFaceService {

    private static final String MODEL_ID = "meta-llama/Llama-3.1-8B-Instruct";
    private static final String HF_API_URL = "https://router.huggingface.co/v1/chat/completions";

    @Value("${huggingface.api.token}")
    private String apiToken;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // =========================================================
    // FEATURE 1 — Génération de description de poste (existante)
    // =========================================================

    public String generateJobDescription(String titre, String skills,
                                         String typeContrat, String localisation) {
        try {
            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "model", MODEL_ID,
                    "messages", List.of(
                            Map.of("role", "system", "content",
                                    "Tu es un expert RH spécialisé dans la rédaction d'offres d'emploi en Tunisie. " +
                                            "Réponds toujours en français. Sois professionnel et concis."),
                            Map.of("role", "user", "content",
                                    buildJobDescriptionPrompt(titre, skills, typeContrat, localisation))
                    ),
                    "max_tokens", 500,
                    "temperature", 0.7
            ));

            return callHuggingFace(requestBody);

        } catch (Exception e) {
            return "Erreur lors de la génération IA : " + e.getMessage();
        }
    }

    private String buildJobDescriptionPrompt(String titre, String skills,
                                             String typeContrat, String localisation) {
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

    // =========================================================
    // FEATURE 2 — Génération des clauses du contrat (NOUVEAU)
    // =========================================================

    /**
     * Génère automatiquement les clauses juridiques du contrat de travail
     * en fonction du type de contrat, du salaire et de la période d'essai.
     *
     * @param typeContrat   CDI / CDD / CIVP / STAGE / ALTERNANCE
     * @param salaire       Salaire final en dinars tunisiens
     * @param periodeEssai  Durée de la période d'essai en mois
     * @param posteExact    Intitulé exact du poste
     * @return Clauses juridiques rédigées en français
     */
    public String generateContractClauses(String typeContrat, Double salaire,
                                          Integer periodeEssai, String posteExact) {
        try {
            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "model", MODEL_ID,
                    "messages", List.of(
                            Map.of("role", "system", "content",
                                    "Tu es un expert juridique spécialisé en droit du travail tunisien. " +
                                            "Tu rédiges des clauses contractuelles professionnelles, claires et conformes " +
                                            "au Code du Travail tunisien. Réponds uniquement en français. " +
                                            "Sois concis, formel et structuré par articles numérotés."),
                            Map.of("role", "user", "content",
                                    buildContractClausesPrompt(typeContrat, salaire, periodeEssai, posteExact))
                    ),
                    "max_tokens", 700,
                    "temperature", 0.4  // Moins créatif = plus formel pour un contrat
            ));

            return callHuggingFace(requestBody);

        } catch (Exception e) {
            return "Erreur lors de la génération des clauses : " + e.getMessage();
        }
    }

    private String buildContractClausesPrompt(String typeContrat, Double salaire,
                                              Integer periodeEssai, String posteExact) {
        return String.format(
                "Rédige les clauses principales d'un contrat de travail tunisien avec les informations suivantes :\n" +
                        "- Type de contrat : %s\n" +
                        "- Poste : %s\n" +
                        "- Salaire mensuel brut : %.0f dinars tunisiens (DT)\n" +
                        "- Période d'essai : %d mois\n\n" +
                        "Rédige exactement ces 4 articles :\n" +
                        "Article 1 — Objet du contrat\n" +
                        "Article 2 — Durée et prise de poste\n" +
                        "Article 3 — Période d'essai\n" +
                        "Article 4 — Rémunération\n\n" +
                        "Chaque article doit faire 2-3 phrases formelles. " +
                        "Commence directement par 'Article 1' sans introduction.",
                typeContrat, posteExact, salaire, periodeEssai
        );
    }

    // =========================================================
    // MÉTHODE COMMUNE — Appel HTTP HuggingFace
    // =========================================================

    private String callHuggingFace(String requestBody) throws Exception {
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