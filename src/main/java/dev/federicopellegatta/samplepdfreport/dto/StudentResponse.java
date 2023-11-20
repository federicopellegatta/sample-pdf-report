package dev.federicopellegatta.samplepdfreport.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.federicopellegatta.samplepdfreport.entity.Gender;
import dev.federicopellegatta.samplepdfreport.entity.MarkEntity;
import dev.federicopellegatta.samplepdfreport.entity.StudentEntity;
import dev.federicopellegatta.samplepdfreport.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentResponse {
	private String name;
	private String surname;
	private Gender gender;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Rome")
	private LocalDate birthDate;
	private String email;
	private String phoneNumber;
	private AddressResponse address;
	private Recap recap;
	private Collection<SubjectResponse> subjects;
	
	public StudentResponse(StudentEntity studentEntity) {
		this.name = studentEntity.getName();
		this.surname = studentEntity.getSurname();
		this.gender = studentEntity.getGender();
		this.birthDate = studentEntity.getBirthDate();
		this.email = studentEntity.getEmail();
		this.phoneNumber = studentEntity.getPhoneNumber();
		this.address = new AddressResponse(studentEntity.getAddress());
		
		Map<Subject, List<MarkEntity>> marksBySubject = studentEntity.getMarks()
				.stream()
				.collect(Collectors.groupingBy(MarkEntity::getSubject, Collectors.toList()));
		
		this.subjects = marksBySubject.values().stream()
				.map(SubjectResponse::new)
				.toList();
		
		if (!this.subjects.isEmpty())
			this.recap = new Recap(this.subjects);
	}
	
	public String getCompleteName() {
		return name + " " + surname;
	}
	
	public boolean isPassed() {
		return subjects.stream().allMatch(SubjectResponse::isPassed);
	}
	
	public Collection<SubjectResponse> getFailedSubjects() {
		return subjects.stream().filter(s -> !s.isPassed()).toList();
	}
}
