package com.trustedwork.job.service;

import com.trustedwork.job.dto.request.ApplicationCreateRequest;
import com.trustedwork.job.dto.request.ApplicationUpdateRequest;
import com.trustedwork.job.dto.response.ApplicationResponse;

import java.util.List;

public interface ApplicationService {
	ApplicationResponse create(ApplicationCreateRequest request);

	ApplicationResponse getById(Long id);

	List<ApplicationResponse> getByJobPostId(Long jobPostId);

	ApplicationResponse update(Long id, ApplicationUpdateRequest request);

	void delete(Long id);
}
