package com.trustedwork.job.repository;

import com.trustedwork.job.entity.JobPost;
import com.trustedwork.job.enums.JobStatus;
import com.trustedwork.job.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
	List<JobPost> findByClientId(Long clientId);

	List<JobPost> findByStatus(JobStatus status);

	List<JobPost> findByType(JobType type);
}
