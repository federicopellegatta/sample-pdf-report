package dev.federicopellegatta.samplepdfreport.repository;

import dev.federicopellegatta.samplepdfreport.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}