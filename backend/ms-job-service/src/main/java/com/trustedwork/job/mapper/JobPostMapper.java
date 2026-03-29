package com.trustedwork.job.mapper;

import com.trustedwork.job.dto.request.JobPostCreateRequest;
import com.trustedwork.job.dto.request.JobPostUpdateRequest;
import com.trustedwork.job.dto.response.JobPostResponse;
import com.trustedwork.job.entity.JobPost;

public final class JobPostMapper {
	private JobPostMapper() {
	}

	public static JobPost toEntity(JobPostCreateRequest dto) {
		if (dto == null) {
			return null;
		}
		return JobPost.builder()
				.clientId(dto.getClientId())
				.title(dto.getTitle())
				.description(dto.getDescription())
				.type(dto.getType())
				.category(dto.getCategory())
				.budget(dto.getBudget())
				.requiredSkills(dto.getRequiredSkills())
				.region(dto.getRegion())
				.deadline(dto.getDeadline())
				.status(dto.getStatus())
				.build();
	}

	public static void updateEntity(JobPostUpdateRequest dto, JobPost entity) {
		if (dto == null || entity == null) {
			return;
		}
		entity.setClientId(dto.getClientId());
		entity.setTitle(dto.getTitle());
		entity.setDescription(dto.getDescription());
		entity.setType(dto.getType());
		entity.setCategory(dto.getCategory());
		entity.setBudget(dto.getBudget());
		entity.setRequiredSkills(dto.getRequiredSkills());
		entity.setRegion(dto.getRegion());
		entity.setDeadline(dto.getDeadline());
		entity.setStatus(dto.getStatus());
		entity.setIsClientFlagged(dto.getIsClientFlagged());
		entity.setAiEnhanced(dto.getAiEnhanced());
	}

	public static JobPostResponse toResponse(JobPost entity) {
		if (entity == null) {
			return null;
		}
		return JobPostResponse.builder()
				.id(entity.getId())
				.clientId(entity.getClientId())
				.title(entity.getTitle())
				.description(entity.getDescription())
				.type(entity.getType())
				.category(entity.getCategory())
				.budget(entity.getBudget())
				.requiredSkills(entity.getRequiredSkills())
				.region(entity.getRegion())
				.deadline(entity.getDeadline())
				.status(entity.getStatus())
				.isClientFlagged(entity.getIsClientFlagged())
				.aiEnhanced(entity.getAiEnhanced())
				.createdAt(entity.getCreatedAt())
				.build();
	}
}
