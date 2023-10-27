package dev.federicopellegatta.samplepdfreport.dto;

import dev.federicopellegatta.samplepdfreport.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubjectResponse {
	private Subject subject;
	private Collection<MarkResponse> marks;
}
