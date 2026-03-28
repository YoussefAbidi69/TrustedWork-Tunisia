package tn.esprit.reviewservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.reviewservice.dto.request.AddXpRequest;
import tn.esprit.reviewservice.dto.response.GrowthProfileResponse;
import tn.esprit.reviewservice.entity.GrowthProfile;
import tn.esprit.reviewservice.exception.GrowthProfileNotFoundException;
import tn.esprit.reviewservice.mapper.ReviewMapper;
import tn.esprit.reviewservice.repository.GrowthProfileRepository;
import tn.esprit.reviewservice.service.interfaces.IGrowthProfileService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GrowthProfileServiceImpl implements IGrowthProfileService {

    private final GrowthProfileRepository growthProfileRepository;
    private final ReviewMapper mapper;

    @Override
    public GrowthProfileResponse initializeProfile(Long userId) {
        GrowthProfile existing = growthProfileRepository.findByUserId(userId).orElse(null);

        if (existing != null) {
            return mapper.toGrowthProfileResponse(existing);
        }

        GrowthProfile profile = GrowthProfile.builder()
                .userId(userId)
                .xp(0)
                .level(1)
                .streakDays(0)
                .longestStreak(0)
                .badgesCount(0)
                .profileCompleted(false)
                .lastActivityDate(LocalDate.now())
                .build();

        GrowthProfile saved = growthProfileRepository.saveAndFlush(profile);
        return mapper.toGrowthProfileResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GrowthProfileResponse getByUserId(Long userId) {
        GrowthProfile profile = growthProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new GrowthProfileNotFoundException(
                        "GrowthProfile introuvable pour userId = " + userId
                ));

        return mapper.toGrowthProfileResponse(profile);
    }

    @Override
    public GrowthProfileResponse addXp(AddXpRequest request) {
        GrowthProfile profile = growthProfileRepository.findByUserId(request.getUserId())
                .orElseGet(() -> growthProfileRepository.saveAndFlush(
                        GrowthProfile.builder()
                                .userId(request.getUserId())
                                .xp(0)
                                .level(1)
                                .streakDays(0)
                                .longestStreak(0)
                                .badgesCount(0)
                                .profileCompleted(false)
                                .lastActivityDate(LocalDate.now())
                                .build()
                ));

        int currentXp = profile.getXp() != null ? profile.getXp() : 0;
        int xpToAdd = request.getXpToAdd() != null ? request.getXpToAdd() : 0;

        profile.setXp(currentXp + xpToAdd);

        updateStreak(profile);

        GrowthProfile saved = growthProfileRepository.save(profile);
        return mapper.toGrowthProfileResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrowthProfileResponse> getLeaderboard() {
        return growthProfileRepository.findAllByOrderByXpDesc()
                .stream()
                .map(mapper::toGrowthProfileResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrowthProfileResponse> getAllProfiles() {
        return growthProfileRepository.findAll()
                .stream()
                .map(mapper::toGrowthProfileResponse)
                .collect(Collectors.toList());
    }

    private void updateStreak(GrowthProfile profile) {
        LocalDate today = LocalDate.now();
        LocalDate lastActivity = profile.getLastActivityDate();

        if (lastActivity == null) {
            profile.setStreakDays(1);
        } else if (lastActivity.equals(today.minusDays(1))) {
            profile.setStreakDays((profile.getStreakDays() != null ? profile.getStreakDays() : 0) + 1);
        } else if (!lastActivity.equals(today)) {
            profile.setStreakDays(1);
        }

        if (profile.getLongestStreak() == null || profile.getStreakDays() > profile.getLongestStreak()) {
            profile.setLongestStreak(profile.getStreakDays());
        }

        profile.setLastActivityDate(today);
    }
}