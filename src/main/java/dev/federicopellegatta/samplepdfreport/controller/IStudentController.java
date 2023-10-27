package dev.federicopellegatta.samplepdfreport.controller;

import dev.federicopellegatta.samplepdfreport.entity.StudentEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@RequestMapping(value = "/api/v1/student")
@ComponentScan(lazyInit = true)
@Tag(name = "Student controller", description = "School register controller which manages students")
public interface IStudentController {
	
	@GetMapping
	ResponseEntity<Collection<StudentEntity>> allStudents();
}
