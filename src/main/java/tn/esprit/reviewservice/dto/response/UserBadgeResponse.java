package tn.esprit.reviewservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadgeResponse {

    private Long id;
    private Long userId;
    private Long badgeId;
    private String badgeName;
    private String badgeDescription;
    private Boolean showcased;
    private LocalDateTime earnedAt;
}