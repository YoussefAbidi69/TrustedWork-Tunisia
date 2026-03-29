package com.trustedwork.job.mapper;

import com.trustedwork.job.dto.request.ApplicationCreateRequest;
import com.trustedwork.job.dto.request.ApplicationUpdateRequest;
import com.trustedwork.job.dto.response.ApplicationResponse;
import com.trustedwork.job.entity.Application;
import com.trustedwork.job.entity.JobPost;

public final class ApplicationMapper {
	private ApplicationMapper() {
	}

	public static Application toEntity(ApplicationCreateRequest dto, JobPost jobPost) {
		if (dto == null) {
			return null;
		}
		return Application.builder()
				.jobPost(jobPost)
				.freelancerId(dto.getFreelancerId())
				.coverLetter(dto.getCoverLetter())
				.proposedAmount(dto.getProposedAmount())
				.proposedDeadline(dto.getProposedDeadline())
				.build();
	}

	public static void updateEntity(ApplicationUpdateRequest dto, Application entity) {
		if (dto == null || entity == null) {
			return;
		}
		entity.setStatus(dto.getStatus());
	}

	public static ApplicationResponse toResponse(Application entity) {
		if (entity == null) {
			return null;
		}
		Long jobPostId = entity.getJobPost() != null ? entity.getJobPost().getId() : null;
		return ApplicationResponse.builder()
				.id(entity.getId())
				.jobPostId(jobPostId)
				.freelancerId(entity.getFreelancerId())
				.coverLetter(entity.getCoverLetter())
				.proposedAmount(entity.getProposedAmount())
				.proposedDeadline(entity.getProposedDeadline())
				.status(entity.getStatus())
				.createdAt(entity.getCreatedAt())
				.counterOfferAmount(entity.getCounterOfferAmount())
				.counterOfferNote(entity.getCounterOfferNote())
				.build();
	}
}
