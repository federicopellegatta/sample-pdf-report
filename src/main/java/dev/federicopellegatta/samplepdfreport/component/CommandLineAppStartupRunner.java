package dev.federicopellegatta.samplepdfreport.component;

import dev.federicopellegatta.samplepdfreport.entity.*;
import dev.federicopellegatta.samplepdfreport.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;

@Component
@Slf4j
@AllArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {
	private final StudentRepository studentRepository;
	private final RandomComponent random;
	
	@Override
	public void run(String... args) {
		if (studentRepository.count() == 0) {
			log.info("No students found, creating some");
			studentRepository.saveAll(createStudents());
			log.info(studentRepository.count() + " students created");
		} else {
			log.info(studentRepository.count() + " students found, skipping creation");
		}
	}
	
	private Collection<StudentEntity> createStudents() {
		
		return IntStream.range(0, 100)
				.mapToObj(i -> {
					AddressEntity address = AddressEntity.builder()
							.street("via Roma")
							.number(i)
							.zipCode("20100")
							.city("Milano")
							.country("IT")
							.build();
					
					Collection<MarkEntity> marks = IntStream.range(0, random.integer(30))
							.mapToObj(j -> MarkEntity.builder()
									.subject(random.enumValue(Subject.class))
									.date(random.localDate())
									.examType(random.enumValue(ExamType.class))
									.mark(Math.round(random.gaussian(7, 1.5f) * 2) / 2f)
									.build())
							.toList();
					
					return StudentEntity.builder()
							.name("Name " + i)
							.surname("Surname " + i)
							.email(random.bool() ? "email" + i + "@example.com" : null)
							.birthDate(random.localDate())
							.phoneNumber(random.bool() ? "1234567890" : null)
							.address(address)
							.marks(marks)
							.build();
				})
				.toList();
	}
}
