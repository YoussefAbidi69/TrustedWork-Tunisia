package com.trustedwork.job.controller;

import com.trustedwork.job.dto.request.ApplicationCreateRequest;
import com.trustedwork.job.dto.request.ApplicationUpdateRequest;
import com.trustedwork.job.dto.response.ApplicationResponse;
import com.trustedwork.job.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Application", description = "Job application management")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

	private final ApplicationService applicationService;

	@Operation(summary = "Apply to a job post")
	@PostMapping("/applications")
	public ResponseEntity<ApplicationResponse> create(@Valid @RequestBody ApplicationCreateRequest request) {
		ApplicationResponse created = applicationService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@Operation(summary = "Get application details")
	@GetMapping("/applications/{id}")
	public ResponseEntity<ApplicationResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(applicationService.getById(id));
	}

	@Operation(summary = "List all applications for a job post")
	@GetMapping("/jobs/{id}/applications")
	public ResponseEntity<List<ApplicationResponse>> getByJob(@PathVariable("id") Long jobPostId) {
		return ResponseEntity.ok(applicationService.getByJobPostId(jobPostId));
	}

	@Operation(summary = "Update an application's status")
	@PutMapping("/applications/{id}")
	public ResponseEntity<ApplicationResponse> update(@PathVariable Long id, @Valid @RequestBody ApplicationUpdateRequest request) {
		return ResponseEntity.ok(applicationService.update(id, request));
	}

	@Operation(summary = "Withdraw an application")
	@DeleteMapping("/applications/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		applicationService.delete(id);
	}
}
