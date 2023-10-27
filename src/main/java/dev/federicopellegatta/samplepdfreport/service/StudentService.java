package dev.federicopellegatta.samplepdfreport.service;

import dev.federicopellegatta.samplepdfreport.entity.StudentEntity;
import dev.federicopellegatta.samplepdfreport.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class StudentService {
	private final StudentRepository studentRepository;
	
	public Collection<StudentEntity> allStudents() {
		return studentRepository.findAll();
	}
}
