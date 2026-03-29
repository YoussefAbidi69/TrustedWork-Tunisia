package com.trustedwork.job.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationCreateRequest {

	@NotNull
	private Long jobPostId;

	@NotNull
	private Long freelancerId;

	@NotBlank
	private String coverLetter;

	@NotNull
	@Positive
	private BigDecimal proposedAmount;

	@NotNull
	@Future
	private LocalDate proposedDeadline;
}
