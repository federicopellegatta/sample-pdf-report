package dev.federicopellegatta.samplepdfreport.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.federicopellegatta.samplepdfreport.entity.ExamType;
import dev.federicopellegatta.samplepdfreport.entity.MarkEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MarkResponse {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Rome")
	private LocalDate date;
	private ExamType examType;
	private float mark;
	
	public MarkResponse(MarkEntity markEntity) {
		this.date = markEntity.getDate();
		this.examType = markEntity.getExamType();
		this.mark = markEntity.getMark();
	}
}
