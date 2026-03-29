package com.trustedwork.job.dto.response;

import com.trustedwork.job.enums.JobStatus;
import com.trustedwork.job.enums.JobType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostResponse {
	private Long id;
	private Long clientId;
	private String title;
	private String description;
	private JobType type;
	private String category;
	private BigDecimal budget;
	private String requiredSkills;
	private String region;
	private LocalDate deadline;
	private JobStatus status;
	private Boolean isClientFlagged;
	private Boolean aiEnhanced;
	private LocalDateTime createdAt;
}
