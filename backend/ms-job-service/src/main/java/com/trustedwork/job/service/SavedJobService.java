package com.trustedwork.job.service;

import com.trustedwork.job.dto.response.JobPostResponse;
import com.trustedwork.job.dto.response.SavedJobResponse;

import java.util.List;

public interface SavedJobService {
	SavedJobResponse saveJob(Long jobPostId, Long freelancerId);

	void unsaveJob(Long jobPostId, Long freelancerId);

	List<JobPostResponse> getSavedJobsByFreelancer(Long freelancerId);
}
