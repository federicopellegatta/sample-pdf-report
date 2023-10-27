package dev.federicopellegatta.samplepdfreport.controller;

import dev.federicopellegatta.samplepdfreport.dto.StudentResponse;
import dev.federicopellegatta.samplepdfreport.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class StudentController implements IStudentController {
	private final StudentService studentService;
	@Override
	public ResponseEntity<Collection<StudentResponse>> allStudents() {
		return new ResponseEntity<>(studentService.allStudents(), HttpStatus.OK);
	}
}
