package dev.federicopellegatta.samplepdfreport.controller;

import dev.federicopellegatta.samplepdfreport.service.PdfGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Validated
public class PdfGeneratorController implements IPdfGeneratorController {
	private final PdfGeneratorService pdfGeneratorService;
	
	@Override
	public ResponseEntity<byte[]> generatePdfReport(boolean header, boolean footer, boolean watermark) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		
		String filename = "output.pdf";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity<>(pdfGeneratorService.generatePdfReport(header, footer, watermark), headers,
		                            HttpStatus.OK);
	}
}
