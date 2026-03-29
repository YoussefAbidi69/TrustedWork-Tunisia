package tn.esprit.freelancerprofileservice.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.freelancerprofileservice.repository.FreelancerProfileRepository;
import tn.esprit.freelancerprofileservice.repository.SkillBadgeRepository;
import tn.esprit.freelancerprofileservice.repository.EndorsementRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class CertificationScheduler {

    private final FreelancerProfileRepository profileRepository;
    private final SkillBadgeRepository skillBadgeRepository;
    private final EndorsementRepository endorsementRepository;

    // Toutes les 60 secondes — stats générales
    @Scheduled(fixedRate = 60000)
    public void logPlatformStats() {
        long profiles = profileRepository.count();
        long badges = skillBadgeRepository.count();
        long endorsements = endorsementRepository.count();

        log.info("📊 STATS PLATEFORME — Profils: {} | Badges: {} | Endorsements: {}",
                profiles, badges, endorsements);
    }

    // Tous les jours à minuit — nettoyage endorsements non modérés
    @Scheduled(cron = "0 0 0 * * *")
    public void logUnmoderatedEndorsements() {
        long unmoderated = endorsementRepository
                .findByToProfileIdAndIsModerated(0L, false).size();
        log.info("🔍 MODÉRATION — {} endorsements en attente de validation admin",
                unmoderated);
    }

    // Toutes les 30 minutes — rappel disponibilités
    @Scheduled(fixedRate = 1800000)
    public void checkAvailableFreelancers() {
        long available = profileRepository
                .findByDisponibilite(
                        tn.esprit.freelancerprofileservice.entity.FreelancerProfile
                                .Disponibilite.AVAILABLE)
                .size();
        log.info("✅ DISPONIBILITÉ — {} freelancers disponibles sur la plateforme",
                available);
    }
}