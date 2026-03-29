package com.trustedwork.job.dto.response;

import com.trustedwork.job.enums.ApplicationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {
	private Long id;
	private Long jobPostId;
	private Long freelancerId;
	private String coverLetter;
	private BigDecimal proposedAmount;
	private LocalDate proposedDeadline;
	private ApplicationStatus status;
	private LocalDateTime createdAt;
	private BigDecimal counterOfferAmount;
	private String counterOfferNote;
}
