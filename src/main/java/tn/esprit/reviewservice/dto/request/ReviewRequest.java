package tn.esprit.reviewservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import tn.esprit.reviewservice.entity.enums.ReviewType;

@Getter
@Setter
public class ReviewRequest {

    private Long contractId;
    private Long reviewerId;
    private Long reviewedUserId;
    private ReviewType reviewType;
    private Integer rating;
    private String comment;
}