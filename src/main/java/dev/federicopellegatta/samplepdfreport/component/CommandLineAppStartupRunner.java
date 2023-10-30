package dev.federicopellegatta.samplepdfreport.component;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
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
					Faker faker = new Faker();
					Address fakerAddress = faker.address();
					AddressEntity address = AddressEntity.builder()
							.street(fakerAddress.streetAddress())
							.number(Integer.parseInt(fakerAddress.streetAddressNumber()))
							.zipCode(fakerAddress.zipCode())
							.city(fakerAddress.cityName())
							.country(fakerAddress.country())
							.build();
					
					Collection<MarkEntity> marks = IntStream.range(0, random.integer(30))
							.mapToObj(j -> MarkEntity.builder()
									.subject(random.enumValue(Subject.class))
									.date(random.localDate())
									.examType(random.enumValue(ExamType.class))
									.mark(getMark())
									.build())
							.toList();
					
					Name fakerName = faker.name();
					return StudentEntity.builder()
							.name(fakerName.firstName())
							.surname(fakerName.lastName())
							.gender(random.bool() ? Gender.MALE : Gender.FEMALE)
							.email(random.bool()
							       ? fakerName.firstName() + "." + fakerName.lastName() + "@example.com"
							       : null)
							.birthDate(random.localDate())
							.phoneNumber(random.bool() ? faker.phoneNumber().phoneNumber() : null)
							.address(address)
							.marks(marks)
							.build();
				})
				.toList();
	}
	
	private float getMark() {
		float mark = Math.round(random.gaussian(7, 1.5f) * 2) / 2f;
		
		if (mark < 2)
			return 2;
		else if (mark > 10)
			return 10;
		return mark;
	}
}
