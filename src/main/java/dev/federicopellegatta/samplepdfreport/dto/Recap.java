package dev.federicopellegatta.samplepdfreport.dto;

import dev.federicopellegatta.samplepdfreport.entity.Subject;
import dev.federicopellegatta.samplepdfreport.service.Mark;
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
		this.average = new Mark(avg).getMarkRounded(1);
		this.poorSubjects = subjects.stream()
				.filter(s -> !new Mark(s.getAverage()).isPassed())
				.map(SubjectResponse::getSubject)
				.toList();
		this.isPassed = (float) this.poorSubjects.size() / subjects.size() < 0.3f;
	}
}
