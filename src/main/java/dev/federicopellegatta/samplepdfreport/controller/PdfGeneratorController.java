package dev.federicopellegatta.samplepdfreport.controller;

import dev.federicopellegatta.samplepdfreport.service.PdfGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Validated
public class PdfGeneratorController implements IPdfGeneratorController {
	private final PdfGeneratorService pdfGeneratorService;
	
	@Override
	public ResponseEntity<String> generatePdfReport() {
		return new ResponseEntity<>(pdfGeneratorService.generatePdfReport(), HttpStatus.OK);
	}
}
