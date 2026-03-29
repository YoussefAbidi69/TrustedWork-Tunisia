package com.trustedwork.job.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJobResponse {
	private Long id;
	private Long freelancerId;
	private Long jobPostId;
	private LocalDateTime savedAt;
}
