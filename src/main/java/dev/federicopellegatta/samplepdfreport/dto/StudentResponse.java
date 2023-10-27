package dev.federicopellegatta.samplepdfreport.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.federicopellegatta.samplepdfreport.entity.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentResponse {
	private String name;
	private String surname;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Rome")
	private LocalDate birthDate;
	private String email;
	private String phoneNumber;
	private AddressEntity address;
	private Collection<SubjectResponse> subjects;
}
