package com.trustedwork.job.dto.request;

import com.trustedwork.job.enums.JobStatus;
import com.trustedwork.job.enums.JobType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostUpdateRequest {

	@NotNull
	private Long clientId;

	@NotBlank
	private String title;

	@NotBlank
	private String description;

	@NotNull
	private JobType type;

	@NotBlank
	private String category;

	@NotNull
	@Positive
	private BigDecimal budget;

	@NotBlank
	private String requiredSkills;

	@NotBlank
	private String region;

	@NotNull
	@Future
	private LocalDate deadline;

	@NotNull
	private JobStatus status;

	@NotNull
	private Boolean isClientFlagged;

	@NotNull
	private Boolean aiEnhanced;
}
