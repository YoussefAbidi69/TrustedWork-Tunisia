package tn.esprit.msrecruitmentservice.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.msrecruitmentservice.entities.JobPosition;
import tn.esprit.msrecruitmentservice.entities.JobStatus;
import tn.esprit.msrecruitmentservice.repositories.IJobPositionRepository;
import tn.esprit.msrecruitmentservice.repositories.IRecruitmentApplicationRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Scheduler — Module 7 Recrutement
 *
 * Tâches automatiques planifiées :
 *
 *  1. detectionFaussesOffres()
 *     → Toutes les nuits à 02h00
 *     → Détecte les offres PUBLISHED sans aucune candidature après 7 jours
 *     → Log une alerte et passe le status à PAUSED
 *
 *  2. cloturureOffresExpirees()
 *     → Tous les jours à 00h30
 *     → Ferme automatiquement les offres dont la deadline est dépassée
 *     → Passe le status à CLOSED
 *
 *  3. rapportQuotidien()
 *     → Tous les jours à 08h00 (heure de bureau)
 *     → Affiche un résumé : nombre d'offres actives, candidatures du jour
 */
@Component
public class JobPositionScheduler {

    private static final Logger log = LoggerFactory.getLogger(JobPositionScheduler.class);

    // Seuil : une offre sans candidature après ce nombre de jours = "fausse offre"
    private static final int SEUIL_JOURS_SANS_CANDIDATURE = 7;

    @Autowired
    private IJobPositionRepository jobPositionRepository;

    @Autowired
    private IRecruitmentApplicationRepository applicationRepository;

    // ─────────────────────────────────────────────────────────────
    // TÂCHE 1 — Détection des fausses offres (offres sans candidature)
    // Planification : chaque nuit à 02h00
    // ─────────────────────────────────────────────────────────────

    @Scheduled(cron = "0 0 2 * * *")
    public void detectionFaussesOffres() {
        log.info("[SCHEDULER] ======= Détection des fausses offres — démarrage =======");

        List<JobPosition> offresPubliees = jobPositionRepository.findByStatus(JobStatus.PUBLISHED);

        int compteurFausses = 0;

        for (JobPosition offre : offresPubliees) {

            // Calcul du nombre de jours depuis la création de l'offre
            long joursDepuisCreation = ChronoUnit.DAYS.between(
                    offre.getCreatedAt().toLocalDate(),
                    LocalDate.now()
            );

            // Vérifie s'il y a au moins une candidature pour ce poste
            Long nombreCandidatures = applicationRepository.countByJobPositionId(offre.getId());

            if (joursDepuisCreation >= SEUIL_JOURS_SANS_CANDIDATURE && nombreCandidatures == 0) {

                log.warn("[SCHEDULER][ALERTE] Fausse offre détectée — ID: {} | Titre: '{}' | " +
                                "Publiée depuis {} jours | 0 candidature → passage en PAUSED",
                        offre.getId(), offre.getTitre(), joursDepuisCreation);

                // Mise en pause de l'offre suspecte
                offre.setStatus(JobStatus.PAUSED);
                jobPositionRepository.save(offre);
                compteurFausses++;
            }
        }

        if (compteurFausses == 0) {
            log.info("[SCHEDULER] Aucune fausse offre détectée parmi {} offre(s) publiée(s).",
                    offresPubliees.size());
        } else {
            log.warn("[SCHEDULER] {} fausse(s) offre(s) mise(s) en PAUSED.", compteurFausses);
        }

        log.info("[SCHEDULER] ======= Détection des fausses offres — terminé =======");
    }

    // ─────────────────────────────────────────────────────────────
    // TÂCHE 2 — Clôture automatique des offres expirées
    // Planification : chaque jour à 00h30
    // ─────────────────────────────────────────────────────────────

    @Scheduled(cron = "0 30 0 * * *")
    public void cloturureOffresExpirees() {
        log.info("[SCHEDULER] ======= Clôture des offres expirées — démarrage =======");

        List<JobPosition> offresPubliees = jobPositionRepository.findByStatus(JobStatus.PUBLISHED);
        LocalDate aujourdhui = LocalDate.now();

        int compteurClotures = 0;

        for (JobPosition offre : offresPubliees) {
            if (offre.getDeadline() != null && offre.getDeadline().isBefore(aujourdhui)) {

                log.info("[SCHEDULER] Offre expirée — ID: {} | Titre: '{}' | " +
                                "Deadline: {} → passage en CLOSED",
                        offre.getId(), offre.getTitre(), offre.getDeadline());

                offre.setStatus(JobStatus.CLOSED);
                jobPositionRepository.save(offre);
                compteurClotures++;
            }
        }

        log.info("[SCHEDULER] {} offre(s) clôturée(s) automatiquement.", compteurClotures);
        log.info("[SCHEDULER] ======= Clôture des offres expirées — terminé =======");
    }

    // ─────────────────────────────────────────────────────────────
    // TÂCHE 3 — Rapport quotidien (log de synthèse)
    // Planification : chaque matin à 08h00
    // ─────────────────────────────────────────────────────────────

    @Scheduled(cron = "0 0 8 * * MON-FRI")
    public void rapportQuotidien() {
        log.info("[SCHEDULER] ======= Rapport quotidien TrustedWork Recruitment =======");

        long offresPubliees  = jobPositionRepository.findByStatus(JobStatus.PUBLISHED).size();
        long offresDraft     = jobPositionRepository.findByStatus(JobStatus.DRAFT).size();
        long offresClosed    = jobPositionRepository.findByStatus(JobStatus.CLOSED).size();
        long totalCandidatures = applicationRepository.count();

        log.info("[SCHEDULER] Offres PUBLISHED  : {}", offresPubliees);
        log.info("[SCHEDULER] Offres DRAFT      : {}", offresDraft);
        log.info("[SCHEDULER] Offres CLOSED     : {}", offresClosed);
        log.info("[SCHEDULER] Total candidatures: {}", totalCandidatures);
        log.info("[SCHEDULER] =====================================================");
    }
}