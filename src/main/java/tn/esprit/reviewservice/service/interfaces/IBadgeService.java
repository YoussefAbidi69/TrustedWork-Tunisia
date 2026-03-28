package tn.esprit.reviewservice.service.interfaces;

import tn.esprit.reviewservice.dto.request.BadgeRequest;
import tn.esprit.reviewservice.dto.response.BadgeResponse;

import java.util.List;

public interface IBadgeService {

    BadgeResponse createBadge(BadgeRequest request);

    List<BadgeResponse> getAllBadges();

    List<BadgeResponse> getActiveBadges();
}