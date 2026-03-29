package com.trustedwork.job.service.impl;

import com.trustedwork.common.exception.ConflictException;
import com.trustedwork.common.exception.ResourceNotFoundException;
import com.trustedwork.job.dto.request.ApplicationCreateRequest;
import com.trustedwork.job.dto.request.ApplicationUpdateRequest;
import com.trustedwork.job.dto.response.ApplicationResponse;
import com.trustedwork.job.entity.Application;
import com.trustedwork.job.entity.JobPost;
import com.trustedwork.job.mapper.ApplicationMapper;
import com.trustedwork.job.repository.ApplicationRepository;
import com.trustedwork.job.repository.JobPostRepository;
import com.trustedwork.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

	private final ApplicationRepository applicationRepository;
	private final JobPostRepository jobPostRepository;

	@Override
	public ApplicationResponse create(ApplicationCreateRequest request) {
		if (applicationRepository.existsByFreelancerIdAndJobPostId(request.getFreelancerId(), request.getJobPostId())) {
			throw new ConflictException("Freelancer has already applied to this job post.");
		}

		JobPost jobPost = jobPostRepository.findById(request.getJobPostId())
				.orElseThrow(() -> new ResourceNotFoundException("JobPost not found with id=" + request.getJobPostId()));

		Application entity = ApplicationMapper.toEntity(request, jobPost);
		try {
			Application saved = applicationRepository.save(entity);
			return ApplicationMapper.toResponse(saved);
		} catch (DataIntegrityViolationException ex) {
			// also protects against concurrent race conditions on the unique constraint
			throw new ConflictException("Duplicate application for this freelancer and job post.");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ApplicationResponse getById(Long id) {
		Application entity = applicationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Application not found with id=" + id));
		return ApplicationMapper.toResponse(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ApplicationResponse> getByJobPostId(Long jobPostId) {
		return applicationRepository.findByJobPostId(jobPostId).stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	public ApplicationResponse update(Long id, ApplicationUpdateRequest request) {
		Application entity = applicationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Application not found with id=" + id));
		ApplicationMapper.updateEntity(request, entity);
		Application saved = applicationRepository.save(entity);
		return ApplicationMapper.toResponse(saved);
	}

	@Override
	public void delete(Long id) {
		if (!applicationRepository.existsById(id)) {
			throw new ResourceNotFoundException("Application not found with id=" + id);
		}
		applicationRepository.deleteById(id);
	}
}
