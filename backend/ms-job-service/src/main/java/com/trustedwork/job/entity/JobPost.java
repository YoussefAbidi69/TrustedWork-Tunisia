package com.trustedwork.job.entity;

import com.trustedwork.job.enums.JobStatus;
import com.trustedwork.job.enums.JobType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job_posts")
public class JobPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long clientId;

	@Column(nullable = false)
	private String title;

	@Lob
	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private JobType type;

	@Column(nullable = false)
	private String category;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal budget;

	@Column(nullable = false)
	private String requiredSkills;

	@Column(nullable = false)
	private String region;

	@Column(nullable = false)
	private LocalDate deadline;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private JobStatus status;

	@Column(nullable = false)
	private Boolean isClientFlagged;

	@Column(nullable = false)
	private Boolean aiEnhanced;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
		if (status == null) {
			status = JobStatus.DRAFT;
		}
		if (isClientFlagged == null) {
			isClientFlagged = false;
		}
		if (aiEnhanced == null) {
			aiEnhanced = false;
		}
	}
}
