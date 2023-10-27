package dev.federicopellegatta.samplepdfreport.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "t_mark")
public class MarkEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;
	@Column(name = "subject", nullable = false)
	private Subject subject;
	@Column(name = "date", nullable = false)
	private LocalDate date;
	@Column(name = "exam_type", nullable = false)
	private ExamType examType;
	@Column(name = "mark", nullable = false)
	private float mark;
}