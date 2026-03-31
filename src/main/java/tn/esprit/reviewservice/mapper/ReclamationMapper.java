package tn.esprit.reviewservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.reviewservice.dto.request.ReclamationRequest;
import tn.esprit.reviewservice.dto.response.ReclamationResponse;
import tn.esprit.reviewservice.entity.Reclamation;

@Component
public class ReclamationMapper {

    public Reclamation toEntity(ReclamationRequest request) {
        Reclamation r = new Reclamation();
        r.setReviewId(request.getReviewId());
        r.setReportedByUserId(request.getReportedByUserId());
        r.setMotif(request.getMotif());
        r.setDescription(request.getDescription());
        return r;
    }

    public ReclamationResponse toResponse(Reclamation r) {
        return ReclamationResponse.builder()
                .id(r.getId())
                .reviewId(r.getReviewId())
                .reportedByUserId(r.getReportedByUserId())
                .motif(r.getMotif())
                .description(r.getDescription())
                .status(r.getStatus())
                .createdAt(r.getCreatedAt())
                .resolvedAt(r.getResolvedAt())
                .build();
    }
}