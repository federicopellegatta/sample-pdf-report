package dev.federicopellegatta.samplepdfreport.dto;

import dev.federicopellegatta.samplepdfreport.utils.MathUtils;
import lombok.Data;

@Data
public class SubjectStatistics {
	private final float average;
	private final int numberOfPassedStudents;
	private final int numberOfFailedStudents;
	private final float percentageOfPassedStudents;
	private final float percentageOfFailedStudents;
	
	public SubjectStatistics(float average, int numberOfPassedStudents, int numberOfFailedStudents) {
		this.average = average;
		this.numberOfPassedStudents = numberOfPassedStudents;
		this.numberOfFailedStudents = numberOfFailedStudents;
		int studentsNumber = numberOfPassedStudents + numberOfFailedStudents;
		this.percentageOfPassedStudents = MathUtils.round((float) numberOfPassedStudents * 100 / studentsNumber, 1);
		this.percentageOfFailedStudents = MathUtils.round((float) numberOfFailedStudents * 100 / studentsNumber, 1);
	}
}
