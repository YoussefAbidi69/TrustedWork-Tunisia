package com.trustedwork.job.controller;

import com.trustedwork.job.dto.request.JobPostCreateRequest;
import com.trustedwork.job.dto.request.JobPostUpdateRequest;
import com.trustedwork.job.dto.response.JobPostResponse;
import com.trustedwork.job.service.JobPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "JobPost", description = "Job post management")
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobPostController {

	private final JobPostService jobPostService;

	@Operation(summary = "Create a job post")
	@PostMapping
	public ResponseEntity<JobPostResponse> create(@Valid @RequestBody JobPostCreateRequest request) {
		JobPostResponse created = jobPostService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@Operation(summary = "List all job posts")
	@GetMapping
	public ResponseEntity<List<JobPostResponse>> getAll() {
		return ResponseEntity.ok(jobPostService.getAll());
	}

	@Operation(summary = "Get job post details")
	@GetMapping("/{id}")
	public ResponseEntity<JobPostResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(jobPostService.getById(id));
	}

	@Operation(summary = "Update a job post")
	@PutMapping("/{id}")
	public ResponseEntity<JobPostResponse> update(@PathVariable Long id, @Valid @RequestBody JobPostUpdateRequest request) {
		return ResponseEntity.ok(jobPostService.update(id, request));
	}

	@Operation(summary = "Delete a job post")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		jobPostService.delete(id);
	}
}
