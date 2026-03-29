package com.trustedwork.job.repository;

import com.trustedwork.job.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
	List<Application> findByJobPostId(Long jobPostId);

	boolean existsByFreelancerIdAndJobPostId(Long freelancerId, Long jobPostId);

	Optional<Application> findByFreelancerIdAndJobPostId(Long freelancerId, Long jobPostId);
}
