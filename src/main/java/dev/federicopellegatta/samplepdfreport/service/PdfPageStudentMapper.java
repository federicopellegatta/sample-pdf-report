package dev.federicopellegatta.samplepdfreport.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class PdfPageStudentMapper {
	private final PDDocument document;
	private final int minParallelPages;
	
	public PdfPageStudentMapper(PDDocument document) {
		this.document = document;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try {
			properties.load(loader.getResourceAsStream("application.yml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.minParallelPages = Integer.parseInt(properties.getProperty("pdf.mapper.min-parallel-pages", "10"));
	}
	
	public Map<Integer, String> getPageNumberToStudentNameMap() {
		return document.getNumberOfPages() <= minParallelPages ? createMapNonParallel() : createMapParallel();
	}
	
	private Map<Integer, String> createMapParallel() {
		long startTime = System.nanoTime();
		HashMap<Integer, String> result = IntStream.range(1, document.getNumberOfPages())
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
				                          (a, b) -> {
					                          throw new IllegalStateException("Duplicate page number " + a);
				                          },
				                          HashMap::new));
		long endTime = System.nanoTime();
		log.info("Tempo di esecuzione in parallelo: {} ms", (endTime - startTime) / 1000000);
		return result;
	}
	
	private Map<Integer, String> createMapNonParallel() {
		long startTime = System.nanoTime();
		
		int pageNumber = 1;
		Map<Integer, String> pageNumberToStudentNameMap = new HashMap<>();
		for (PDPage ignored : document.getPages()) {
			String studentName;
			try {
				studentName = getStudentNameAtPageNumber(pageNumber);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			pageNumberToStudentNameMap.put(pageNumber, studentName);
			++pageNumber;
		}
		
		long endTime = System.nanoTime();
		log.info("Tempo di esecuzione non in parallelo: {} ms", (endTime - startTime) / 1000000);
		return pageNumberToStudentNameMap;
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