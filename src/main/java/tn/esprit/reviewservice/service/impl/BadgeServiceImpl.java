package tn.esprit.reviewservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.reviewservice.dto.request.BadgeRequest;
import tn.esprit.reviewservice.dto.response.BadgeResponse;
import tn.esprit.reviewservice.entity.Badge;
import tn.esprit.reviewservice.mapper.ReviewMapper;
import tn.esprit.reviewservice.repository.BadgeRepository;
import tn.esprit.reviewservice.service.interfaces.IBadgeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements IBadgeService {

    private final BadgeRepository badgeRepository;
    private final ReviewMapper mapper;

    @Override
    public BadgeResponse createBadge(BadgeRequest request) {

        Badge badge = Badge.builder()
                .name(request.getName())
                .description(request.getDescription())
                .categorie(request.getCategorie())
                .rarete(request.getRarete())
                .xpRequired(request.getXpRequired())
                .build();

        return mapper.toBadgeResponse(badgeRepository.save(badge));
    }

    @Override
    public List<BadgeResponse> getAllBadges() {
        return badgeRepository.findAll()
                .stream()
                .map(mapper::toBadgeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BadgeResponse> getActiveBadges() {
        return badgeRepository.findByIsActiveTrueOrderByXpRequiredAsc()
                .stream()
                .map(mapper::toBadgeResponse)
                .collect(Collectors.toList());
    }
}