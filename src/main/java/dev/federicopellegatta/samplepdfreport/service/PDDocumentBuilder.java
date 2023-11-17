package dev.federicopellegatta.samplepdfreport.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class PDDocumentBuilder {
	private final PDDocument document;
	
	public PDDocumentBuilder(PDDocument document) {
		this.document = document;
	}
	
	public PDDocumentBuilder addHeader() {
		log.info("Adding header to PDF document");
		int fontSize = 15;
		PDType1Font font = PDType1Font.TIMES_ROMAN;
		
		int pageNumber = 1;
		getPageNumberToStudentNameMap(); // FIXME this will be slow for large PDFs
		Map<Integer, String> studentNames = getPageNumberToStudentNameMapParallel();
		for (PDPage page : document.getPages()) {
			
			try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
			                                                                 PDPageContentStream.AppendMode.APPEND,
			                                                                 true, false)) {
				contentStream.setFont(font, fontSize);
				float startY = page.getCropBox().getUpperRightY() - 30;
				float startX = page.getCropBox().getLowerLeftX() + 55;
				
				// add an image
				PDImageXObject pdImage =
						PDImageXObject.createFromFile("src/main/resources/static/imgs/logo.jpg", document);
				int imageWidth = (int) (page.getCropBox().getWidth() / 20);
				int imageHeight = (int) (page.getCropBox().getWidth() / 20);
				contentStream.drawImage(pdImage, startX, startY - imageHeight, imageWidth, imageHeight);
				
				// add some text
				contentStream.beginText();
				contentStream.newLineAtOffset(startX + imageWidth + 10, startY - fontSize);
				contentStream.showText("Highland College");
				contentStream.showText(" - " + studentNames.getOrDefault(pageNumber, ""));
				contentStream.endText();
				
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
			
			++pageNumber;
		}
		
		return new PDDocumentBuilder(document);
	}
	
	private Map<Integer, String> getPageNumberToStudentNameMapParallel() {
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
	
	private Map<Integer, String> getPageNumberToStudentNameMap() {
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
	
	public PDDocumentBuilder addFooter() {
		log.info("Adding footer to PDF document");
		int fontSize = 12;
		PDType1Font font = PDType1Font.TIMES_ROMAN;
		
		String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		for (PDPage page : document.getPages()) {
			
			try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
			                                                                 PDPageContentStream.AppendMode.APPEND,
			                                                                 true, false)) {
				contentStream.setFont(font, fontSize);
				float y = page.getCropBox().getLowerLeftY() + 25;
				float startX = page.getCropBox().getLowerLeftX() + 40;
				
				contentStream.beginText();
				contentStream.newLineAtOffset(startX, y);
				contentStream.showText("Generated on " + today);
				contentStream.endText();
				
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		return new PDDocumentBuilder(document);
	}
	
	public PDDocumentBuilder addWatermark(String watermark) throws IOException {
		log.info("Adding watermark to PDF document with text: {}", watermark);
		int fontSize = 80;
		PDType1Font font = PDType1Font.TIMES_BOLD;
		
		float textWidth = getTextWidth(font, fontSize, watermark);
		
		for (PDPage page : document.getPages()) {
			
			try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
			                                                                 PDPageContentStream.AppendMode.APPEND,
			                                                                 true, false)) {
				float height = page.getMediaBox().getHeight();
				float width = page.getMediaBox().getWidth();
				
				double beta = Math.atan(height / width);
				double alpha = Math.toRadians(180.0) - beta - Math.toRadians(90);
				double a = textWidth * Math.sin(alpha);
				double b = textWidth * Math.cos(alpha);
				
				Matrix textMatrix = Matrix.getRotateInstance(beta, (float) ((width - a) / 2),
				                                             (float) ((height - b) / 2) - (float) fontSize / 2);
				
				PDExtendedGraphicsState r0 = new PDExtendedGraphicsState();
				r0.setNonStrokingAlphaConstant(0.5f);
				contentStream.setGraphicsStateParameters(r0);
				contentStream.setNonStrokingColor(new Color(155, 155, 155)); // grey
				contentStream.beginText();
				contentStream.setFont(font, fontSize);
				contentStream.setTextMatrix(textMatrix);
				contentStream.showText(watermark);
				contentStream.endText();
				
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		return new PDDocumentBuilder(document);
	}
	
	private float getTextWidth(PDType1Font font, int fontSize, String text) throws IOException {
		return (font.getStringWidth(text) / 1000.0f) * fontSize;
	}
	
	public PDDocument build() {
		return this.document;
	}
}
