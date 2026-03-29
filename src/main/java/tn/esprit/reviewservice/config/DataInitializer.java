package tn.esprit.reviewservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.reviewservice.entity.Badge;
import tn.esprit.reviewservice.entity.enums.CategorieBadge;
import tn.esprit.reviewservice.entity.enums.Rarete;
import tn.esprit.reviewservice.repository.BadgeRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final BadgeRepository badgeRepository;

    @Override
    public void run(String... args) {
        if (badgeRepository.count() == 0) {
            createBadge("Rising Talent", "Premier avis 5 étoiles reçu",
                    CategorieBadge.TRUST, Rarete.COMMON, 100);

            createBadge("Speed Demon", "5 livraisons avant la deadline",
                    CategorieBadge.ACTIVITY, Rarete.RARE, 200);

            createBadge("Streak Master", "30 jours de streak consécutifs",
                    CategorieBadge.ACTIVITY, Rarete.EPIC, 800);

            createBadge("Elite Freelancer", "Niveau ELITE atteint",
                    CategorieBadge.TRUST, Rarete.LEGENDARY, 2000);

            createBadge("Community Star", "Badge spécial d'engagement exceptionnel",
                    CategorieBadge.SPECIAL, Rarete.LEGENDARY, 2000);

            log.info("5 badges par défaut créés avec succès");
        }
    }

    private void createBadge(String name, String description,
                             CategorieBadge categorie, Rarete rarete, int xpRequired) {
        Badge badge = new Badge();
        badge.setName(name);
        badge.setDescription(description);
        badge.setCategorie(categorie);
        badge.setRarete(rarete);
        badge.setXpRequired(xpRequired);
        badge.setIsActive(true);
        badgeRepository.save(badge);
    }
}