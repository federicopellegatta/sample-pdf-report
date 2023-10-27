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
public class Recap {
	private float average;
	private Collection<Subject> poorSubjects;
	private boolean isPassed;
	
	public Recap(Collection<SubjectResponse> subjects) {
		double avg = subjects.stream()
				.mapToDouble(SubjectResponse::getAverage)
				.average()
				.orElse(0);
		this.average = Math.round(avg * 10) / 10f; // round to 1 decimal
		this.poorSubjects = subjects.stream()
				.filter(s -> s.getAverage() < 6)
				.map(SubjectResponse::getSubject)
				.toList();
		this.isPassed = this.poorSubjects.size() < 3;
	}
}
