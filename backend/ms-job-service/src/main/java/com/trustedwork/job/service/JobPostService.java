package com.trustedwork.job.service;

import com.trustedwork.job.dto.request.JobPostCreateRequest;
import com.trustedwork.job.dto.request.JobPostUpdateRequest;
import com.trustedwork.job.dto.response.JobPostResponse;

import java.util.List;

public interface JobPostService {
	JobPostResponse create(JobPostCreateRequest request);

	List<JobPostResponse> getAll();

	JobPostResponse getById(Long id);

	JobPostResponse update(Long id, JobPostUpdateRequest request);

	void delete(Long id);
}
