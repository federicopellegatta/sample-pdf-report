package dev.federicopellegatta.samplepdfreport.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "/api/v1/pdf-generator")
@ComponentScan(lazyInit = true)
@Tag(name = "PDF generator controller", description = "PDF generator controller which generates PDF reports")
public interface IPdfGeneratorController {
	@GetMapping
	ResponseEntity<byte[]> generatePdfReport(
			@RequestParam(name = "header", required = false, defaultValue = "true") boolean header,
			@RequestParam(name = "footer", required = false, defaultValue = "true") boolean footer,
			@RequestParam(name = "watermark", required = false, defaultValue = "false") boolean watermark);
}
