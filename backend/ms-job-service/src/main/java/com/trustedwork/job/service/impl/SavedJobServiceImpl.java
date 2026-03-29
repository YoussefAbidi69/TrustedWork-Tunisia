package com.trustedwork.job.service.impl;

import com.trustedwork.common.exception.ConflictException;
import com.trustedwork.common.exception.ResourceNotFoundException;
import com.trustedwork.job.dto.response.JobPostResponse;
import com.trustedwork.job.dto.response.SavedJobResponse;
import com.trustedwork.job.entity.JobPost;
import com.trustedwork.job.entity.SavedJob;
import com.trustedwork.job.mapper.JobPostMapper;
import com.trustedwork.job.mapper.SavedJobMapper;
import com.trustedwork.job.repository.JobPostRepository;
import com.trustedwork.job.repository.SavedJobRepository;
import com.trustedwork.job.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SavedJobServiceImpl implements SavedJobService {

	private final SavedJobRepository savedJobRepository;
	private final JobPostRepository jobPostRepository;

	@Override
	public SavedJobResponse saveJob(Long jobPostId, Long freelancerId) {
		if (savedJobRepository.existsByFreelancerIdAndJobPostId(freelancerId, jobPostId)) {
			throw new ConflictException("This job post is already saved by this freelancer.");
		}

		JobPost jobPost = jobPostRepository.findById(jobPostId)
				.orElseThrow(() -> new ResourceNotFoundException("JobPost not found with id=" + jobPostId));

		SavedJob entity = SavedJobMapper.toEntity(freelancerId, jobPost);
		try {
			SavedJob saved = savedJobRepository.save(entity);
			return SavedJobMapper.toResponse(saved);
		} catch (DataIntegrityViolationException ex) {
			throw new ConflictException("Duplicate saved job for this freelancer and job post.");
		}
	}

	@Override
	public void unsaveJob(Long jobPostId, Long freelancerId) {
		SavedJob existing = savedJobRepository.findByFreelancerIdAndJobPostId(freelancerId, jobPostId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"SavedJob not found for freelancerId=" + freelancerId + " and jobPostId=" + jobPostId));
		savedJobRepository.delete(existing);
	}

	@Override
	@Transactional(readOnly = true)
	public List<JobPostResponse> getSavedJobsByFreelancer(Long freelancerId) {
		return savedJobRepository.findByFreelancerId(freelancerId)
				.stream()
				.map(SavedJob::getJobPost)
				.map(JobPostMapper::toResponse)
				.toList();
	}
}
