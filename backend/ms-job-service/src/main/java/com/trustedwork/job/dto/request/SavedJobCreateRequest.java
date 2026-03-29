package com.trustedwork.job.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJobCreateRequest {
	@NotNull
	private Long freelancerId;
}
