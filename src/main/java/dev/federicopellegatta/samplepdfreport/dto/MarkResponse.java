package dev.federicopellegatta.samplepdfreport.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.federicopellegatta.samplepdfreport.entity.ExamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
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
	
	public String getFormattedMark() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(1);
		return df.format(this.mark);
	}
}
