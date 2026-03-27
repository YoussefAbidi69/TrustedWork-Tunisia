package tn.esprit.reviewservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.reviewservice.entity.enums.CategorieBadge;
import tn.esprit.reviewservice.entity.enums.Rarete;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeRequest {

    @NotBlank(message = "Badge name is required")
    private String name;

    @NotBlank(message = "Badge description is required")
    private String description;

    @NotNull(message = "Badge category is required")
    private CategorieBadge categorie;

    @NotNull(message = "Badge rarity is required")
    private Rarete rarete;

    @NotNull(message = "XP required is required")
    @Min(value = 0, message = "XP required cannot be negative")
    private Integer xpRequired;
}