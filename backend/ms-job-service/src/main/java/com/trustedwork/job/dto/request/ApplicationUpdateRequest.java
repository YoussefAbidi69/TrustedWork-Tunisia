package com.trustedwork.job.dto.request;

import com.trustedwork.job.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationUpdateRequest {

	@NotNull
	private ApplicationStatus status;
}
