package com.trustedwork.job.mapper;

import com.trustedwork.job.dto.response.SavedJobResponse;
import com.trustedwork.job.entity.JobPost;
import com.trustedwork.job.entity.SavedJob;

public final class SavedJobMapper {
	private SavedJobMapper() {
	}

	public static SavedJob toEntity(Long freelancerId, JobPost jobPost) {
		return SavedJob.builder()
				.freelancerId(freelancerId)
				.jobPost(jobPost)
				.build();
	}

	public static SavedJobResponse toResponse(SavedJob entity) {
		if (entity == null) {
			return null;
		}
		Long jobPostId = entity.getJobPost() != null ? entity.getJobPost().getId() : null;
		return SavedJobResponse.builder()
				.id(entity.getId())
				.freelancerId(entity.getFreelancerId())
				.jobPostId(jobPostId)
				.savedAt(entity.getSavedAt())
				.build();
	}
}
