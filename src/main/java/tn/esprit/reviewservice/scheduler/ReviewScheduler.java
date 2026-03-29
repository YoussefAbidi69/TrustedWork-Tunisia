package tn.esprit.reviewservice.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.reviewservice.entity.GrowthProfile;
import tn.esprit.reviewservice.entity.TrustScore;
import tn.esprit.reviewservice.repository.GrowthProfileRepository;
import tn.esprit.reviewservice.repository.TrustScoreRepository;
import tn.esprit.reviewservice.service.interfaces.ITrustScoreService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewScheduler {

    private final TrustScoreRepository trustScoreRepository;
    private final GrowthProfileRepository growthProfileRepository;
    private final ITrustScoreService trustScoreService;

    /**
     * Scheduler 1 : recalcul des trust scores
     * Exécution : chaque jour à 3h du matin
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void recalculateTrustScores() {
        log.info("=== SCHEDULER: Recalcul TrustScore lancé à {} ===", LocalDateTime.now());

        List<TrustScore> allScores = trustScoreRepository.findAll();
        int count = 0;

        for (TrustScore ts : allScores) {
            try {
                trustScoreService.recalculateTrustScore(ts.getUserId());
                count++;
            } catch (Exception e) {
                log.error("Erreur recalcul TrustScore userId={} : {}", ts.getUserId(), e.getMessage());
            }
        }

        log.info("=== SCHEDULER: {} TrustScores recalculés ===", count);
    }

    /**
     * Scheduler 2 : vérification des streaks
     * Exécution : chaque jour à minuit
     * Si la dernière activité est avant hier ou plus ancienne, streak reset à 0
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void verifyStreaks() {
        log.info("=== SCHEDULER: Vérification des streaks lancée à {} ===", LocalDateTime.now());

        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<GrowthProfile> profiles = growthProfileRepository.findAll();
        int resetCount = 0;

        for (GrowthProfile gp : profiles) {
            if (gp.getStreakDays() != null
                    && gp.getStreakDays() > 0
                    && gp.getLastActivityDate() != null
                    && gp.getLastActivityDate().isBefore(yesterday)) {

                gp.setStreakDays(0);
                growthProfileRepository.save(gp);
                resetCount++;

                log.info("Streak reset: userId={} (lastActivityDate={})",
                        gp.getUserId(), gp.getLastActivityDate());
            }
        }

        log.info("=== SCHEDULER: {} streaks réinitialisés ===", resetCount);
    }
}