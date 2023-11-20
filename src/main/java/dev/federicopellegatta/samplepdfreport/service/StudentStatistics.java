package dev.federicopellegatta.samplepdfreport.service;

import dev.federicopellegatta.samplepdfreport.dto.StudentResponse;
import dev.federicopellegatta.samplepdfreport.dto.SubjectResponse;
import dev.federicopellegatta.samplepdfreport.dto.SubjectStatistics;
import dev.federicopellegatta.samplepdfreport.entity.Subject;
import dev.federicopellegatta.samplepdfreport.utils.MathUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
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
		return MathUtils.round((float) getNumberOfPassedStudents() / getNumberOfStudents() * 100, 2);
	}
	
	public double getPercentageOfFailedStudents() {
		return MathUtils.round((float) getNumberOfFailedStudents() / getNumberOfStudents() * 100, 2);
	}
	
	public Map<Subject, SubjectStatistics> getSubjectStatisticsMap() {
		Map<Subject, Float> averageMarkBySubject = getAverageMarkBySubject();
		Map<Subject, Integer> numberOfPassedStudentsBySubject = getNumberOfPassedStudentsBySubject();
		Map<Subject, Integer> numberOfFailedStudentsBySubject = getNumberOfFailedStudentsBySubject();
		
		return Arrays.stream(Subject.values())
				.collect(Collectors.toMap(
						Function.identity(),
						subject -> new SubjectStatistics(
								averageMarkBySubject.get(subject),
								numberOfPassedStudentsBySubject.get(subject),
								numberOfFailedStudentsBySubject.get(subject)
						)
				));
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
