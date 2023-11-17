package dev.federicopellegatta.samplepdfreport.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class PdfPageStudentMapper {
	private final PDDocument document;
	
	public PdfPageStudentMapper(PDDocument document) {
		this.document = document;
	}
	
	public Map<Integer, String> getPageNumberToStudentNameMap() {
		long startTime = System.nanoTime();
		Map<Integer, String> result = IntStream.range(1, document.getNumberOfPages())
				.parallel()
				.mapToObj(pageNumber -> {
					try {
						return Map.entry(pageNumber, getStudentNameAtPageNumber(pageNumber));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				})
				.collect(Collectors.toMap(Map.Entry::getKey,
				                          Map.Entry::getValue,
				                          (i, j) -> {
					                          throw new IllegalStateException("Duplicate page number " + i);
				                          },
				                          HashMap::new));
		long endTime = System.nanoTime();
		log.info("Time taken to map page number to student name: {} ms", (endTime - startTime) / 1000000);
		return result;
	}
	
	private String getStudentNameAtPageNumber(int pageNumber) throws IOException {
		if (pageNumber - 1 < 0) return "";
		
		PDFTextStripper reader = new PDFTextStripper();
		reader.setStartPage(pageNumber);
		reader.setEndPage(pageNumber);
		String pageText = reader.getText(document);
		Optional<String> studentNameOpt = getStudentNameFromPageText(pageText);
		
		return studentNameOpt.orElse(getStudentNameAtPageNumber(pageNumber - 1));
	}
	
	private Optional<String> getStudentNameFromPageText(String pageText) {
		String[] lines = pageText.split("\n");
		if (lines.length > 2 && lines[0].contains("Page") && lines[2].isBlank())
			return Optional.of(lines[1].trim());
		
		return Optional.empty();
	}
}