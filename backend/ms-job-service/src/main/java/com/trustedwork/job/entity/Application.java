package com.trustedwork.job.entity;

import com.trustedwork.job.enums.ApplicationStatus;
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
@Table(
		name = "applications",
		uniqueConstraints = {
				@UniqueConstraint(name = "uk_application_freelancer_job", columnNames = {"freelancer_id", "job_post_id"})
		}
)
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "job_post_id", nullable = false, foreignKey = @ForeignKey(name = "fk_application_job_post"))
	private JobPost jobPost;

	@Column(name = "freelancer_id", nullable = false)
	private Long freelancerId;

	@Lob
	@Column(nullable = false, columnDefinition = "TEXT")
	private String coverLetter;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal proposedAmount;

	@Column(nullable = false)
	private LocalDate proposedDeadline;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApplicationStatus status;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(precision = 19, scale = 2)
	private BigDecimal counterOfferAmount;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String counterOfferNote;

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
		if (status == null) {
			status = ApplicationStatus.PENDING;
		}
	}
}
