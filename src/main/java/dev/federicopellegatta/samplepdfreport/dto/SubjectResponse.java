package dev.federicopellegatta.samplepdfreport.dto;

import dev.federicopellegatta.samplepdfreport.entity.MarkEntity;
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
	
	/**
	 * Constructor for SubjectResponse using a collection of {@link MarkEntity} with the same {@link Subject}.
	 *
	 * @param markEntities a collection of {@link MarkEntity} with the same {@link Subject}.
	 */
	public SubjectResponse(Collection<MarkEntity> markEntities) {
		this.subject = markEntities.stream()
				.findFirst()
				.map(MarkEntity::getSubject)
				.orElseThrow(() -> new IllegalArgumentException("Cannot create a SubjectResponse without a Subject"));
		this.marks = markEntities.stream().map(MarkResponse::new).toList();
	}
}
