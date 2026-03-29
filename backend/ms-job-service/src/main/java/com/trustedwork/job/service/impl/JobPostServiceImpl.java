package com.trustedwork.job.service.impl;

import com.trustedwork.common.exception.ResourceNotFoundException;
import com.trustedwork.job.dto.request.JobPostCreateRequest;
import com.trustedwork.job.dto.request.JobPostUpdateRequest;
import com.trustedwork.job.dto.response.JobPostResponse;
import com.trustedwork.job.entity.JobPost;
import com.trustedwork.job.mapper.JobPostMapper;
import com.trustedwork.job.repository.JobPostRepository;
import com.trustedwork.job.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobPostServiceImpl implements JobPostService {

	private final JobPostRepository jobPostRepository;

	@Override
	public JobPostResponse create(JobPostCreateRequest request) {
		JobPost entity = JobPostMapper.toEntity(request);
		JobPost saved = jobPostRepository.save(entity);
		return JobPostMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<JobPostResponse> getAll() {
		return jobPostRepository.findAll().stream().map(JobPostMapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public JobPostResponse getById(Long id) {
		JobPost entity = jobPostRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("JobPost not found with id=" + id));
		return JobPostMapper.toResponse(entity);
	}

	@Override
	public JobPostResponse update(Long id, JobPostUpdateRequest request) {
		JobPost entity = jobPostRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("JobPost not found with id=" + id));

		JobPostMapper.updateEntity(request, entity);
		JobPost saved = jobPostRepository.save(entity);
		return JobPostMapper.toResponse(saved);
	}

	@Override
	public void delete(Long id) {
		if (!jobPostRepository.existsById(id)) {
			throw new ResourceNotFoundException("JobPost not found with id=" + id);
		}
		jobPostRepository.deleteById(id);
	}
}
