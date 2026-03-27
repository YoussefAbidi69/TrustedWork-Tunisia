package tn.esprit.reviewservice.dto.response;

import lombok.*;
import tn.esprit.reviewservice.entity.enums.CategorieBadge;
import tn.esprit.reviewservice.entity.enums.Rarete;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeResponse {

    private Long id;
    private String name;
    private String description;
    private CategorieBadge categorie;
    private Rarete rarete;
    private Integer xpRequired;
    private Boolean isActive;
}