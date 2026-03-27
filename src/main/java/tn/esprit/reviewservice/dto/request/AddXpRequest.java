package tn.esprit.reviewservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddXpRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "XP amount is required")
    @Min(value = 1, message = "XP amount must be greater than 0")
    private Integer xpToAdd;
}