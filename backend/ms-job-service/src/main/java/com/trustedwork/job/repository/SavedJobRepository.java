package com.trustedwork.job.repository;

import com.trustedwork.job.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
	List<SavedJob> findByFreelancerId(Long freelancerId);

	boolean existsByFreelancerIdAndJobPostId(Long freelancerId, Long jobPostId);

	Optional<SavedJob> findByFreelancerIdAndJobPostId(Long freelancerId, Long jobPostId);

	void deleteByFreelancerIdAndJobPostId(Long freelancerId, Long jobPostId);
}
