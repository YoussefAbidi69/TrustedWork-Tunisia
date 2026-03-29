package com.trustedwork.job.controller;

import com.trustedwork.job.dto.request.SavedJobCreateRequest;
import com.trustedwork.job.dto.response.JobPostResponse;
import com.trustedwork.job.dto.response.SavedJobResponse;
import com.trustedwork.job.service.SavedJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SavedJob", description = "Saved jobs management")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SavedJobController {

	private final SavedJobService savedJobService;

	@Operation(summary = "Save a job post")
	@PostMapping("/jobs/{jobId}/save")
	public ResponseEntity<SavedJobResponse> save(@PathVariable Long jobId, @Valid @RequestBody SavedJobCreateRequest request) {
		SavedJobResponse created = savedJobService.saveJob(jobId, request.getFreelancerId());
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@Operation(summary = "Remove a job post from saved jobs")
	@DeleteMapping("/jobs/{jobId}/save")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unsave(@PathVariable Long jobId, @RequestParam Long freelancerId) {
		savedJobService.unsaveJob(jobId, freelancerId);
	}

	@Operation(summary = "List saved job posts for a freelancer")
	@GetMapping("/freelancers/{freelancerId}/saved-jobs")
	public ResponseEntity<List<JobPostResponse>> getSaved(@PathVariable Long freelancerId) {
		return ResponseEntity.ok(savedJobService.getSavedJobsByFreelancer(freelancerId));
	}
}
