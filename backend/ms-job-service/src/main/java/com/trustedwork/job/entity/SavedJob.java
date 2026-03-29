package com.trustedwork.job.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "saved_jobs",
		uniqueConstraints = {
				@UniqueConstraint(name = "uk_saved_job_freelancer_job", columnNames = {"freelancer_id", "job_post_id"})
		}
)
public class SavedJob {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "freelancer_id", nullable = false)
	private Long freelancerId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "job_post_id", nullable = false, foreignKey = @ForeignKey(name = "fk_saved_job_job_post"))
	private JobPost jobPost;

	@Column(nullable = false)
	private LocalDateTime savedAt;

	@PrePersist
	public void prePersist() {
		if (savedAt == null) {
			savedAt = LocalDateTime.now();
		}
	}
}
