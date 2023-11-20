package dev.federicopellegatta.samplepdfreport.service;

import dev.federicopellegatta.samplepdfreport.dto.StudentResponse;
import dev.federicopellegatta.samplepdfreport.dto.SubjectResponse;
import dev.federicopellegatta.samplepdfreport.entity.Subject;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentStatistics {
	private final Collection<StudentResponse> students;
	
	public StudentStatistics(Collection<StudentResponse> students) {
		this.students = students;
	}
	
	public int getNumberOfStudents() {
		return students.size();
	}
	
	public long getNumberOfPassedStudents() {
		return students.stream().filter(StudentResponse::isPassed).count();
	}
	
	public long getNumberOfFailedStudents() {
		return students.stream().filter(s -> !s.isPassed()).count();
	}
	
	public double getPercentageOfPassedStudents() {
		return (double) getNumberOfPassedStudents() / getNumberOfStudents() * 100;
	}
	
	public double getPercentageOfFailedStudents() {
		return (double) getNumberOfFailedStudents() / getNumberOfStudents() * 100;
	}
	
	public double getAverageMark() {
		double averageMark = students.stream()
				.map(StudentResponse::getSubjects)
				.flatMap(Collection::stream)
				.mapToDouble(SubjectResponse::getAverage)
				.average()
				.orElse(0);
		return new Mark(averageMark).getMarkRounded(2);
	}
	
	public Map<Subject, Float> getAverageMarkBySubject() {
		return students.stream()
				.map(StudentResponse::getSubjects)
				.flatMap(Collection::stream)
				.collect(
						Collectors.groupingBy(
								SubjectResponse::getSubject,
								Collectors.collectingAndThen(
										Collectors.averagingDouble(SubjectResponse::getAverage),
										average -> new Mark(average).getMarkRounded(2)
								)
						)
				);
	}
	
	public Map<Subject, Integer> getNumberOfPassedStudentsBySubject() {
		return students.stream()
				.map(StudentResponse::getSubjects)
				.flatMap(Collection::stream)
				.filter(SubjectResponse::isPassed)
				.collect(
						Collectors.groupingBy(
								SubjectResponse::getSubject,
								Collectors.summingInt(s -> 1)
						)
				);
	}
	
	public Map<Subject, Integer> getNumberOfFailedStudentsBySubject() {
		return students.stream()
				.map(StudentResponse::getSubjects)
				.flatMap(Collection::stream)
				.filter(s -> !s.isPassed())
				.collect(
						Collectors.groupingBy(
								SubjectResponse::getSubject,
								Collectors.summingInt(s -> 1)
						)
				);
	}
	
}
