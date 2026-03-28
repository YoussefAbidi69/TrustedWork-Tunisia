package tn.esprit.reviewservice.service.interfaces;

import tn.esprit.reviewservice.dto.request.AddXpRequest;
import tn.esprit.reviewservice.dto.response.GrowthProfileResponse;

import java.util.List;

public interface IGrowthProfileService {

    GrowthProfileResponse initializeProfile(Long userId);

    GrowthProfileResponse getByUserId(Long userId);

    GrowthProfileResponse addXp(AddXpRequest request);

    List<GrowthProfileResponse> getLeaderboard();

    List<GrowthProfileResponse> getAllProfiles();
}