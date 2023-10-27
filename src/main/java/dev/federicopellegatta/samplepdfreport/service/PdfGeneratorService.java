package dev.federicopellegatta.samplepdfreport.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import dev.federicopellegatta.samplepdfreport.dto.StudentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class PdfGeneratorService {
	private final StudentService studentService;
	
	public byte[] generatePdfReport() {
		Collection<StudentResponse> students = studentService.allStudents();
		String html = parseHtmlTemplate(students);
		
		return savePdfDocument(html);
	}
	
	private byte[] savePdfDocument(String html) {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			PdfRendererBuilder builder = new PdfRendererBuilder();
			builder.withHtmlContent(html, PdfGeneratorService.class.getResource("/templates/imgs").toString());
			builder.useFastMode();
			builder.toStream(os);
			builder.run();
			
			return os.toByteArray();
		} catch (IOException ex) {
			log.error("Unable to generate PDF!", ex);
			throw new RuntimeException(ex);
		}
	}
	
	private String parseHtmlTemplate(Collection<StudentResponse> students) {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/"); // thymeleaf searches for templates in resources/templates
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setDialect(new SpringStandardDialect());
		templateEngine.setTemplateResolver(templateResolver);
		
		Context context = new Context();
		context.setVariable("class", "4D");
		context.setVariable("header", new ReportHeader("School register", "A students report"));
		context.setVariable("students", students);
		
		return templateEngine.process("school_register", context);
	}
}
