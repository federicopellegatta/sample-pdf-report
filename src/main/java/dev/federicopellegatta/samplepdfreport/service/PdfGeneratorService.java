package dev.federicopellegatta.samplepdfreport.service;

import dev.federicopellegatta.samplepdfreport.dto.StudentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class PdfGeneratorService {
	private final StudentService studentService;
	
	public String generatePdfReport() {
		Collection<StudentResponse> students = studentService.allStudents();
		String html = parseHtmlTemplate(students);
		
		return "";
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
