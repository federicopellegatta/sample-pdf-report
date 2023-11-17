package dev.federicopellegatta.samplepdfreport.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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
		float marginX = 55f;
		float marginY = 30f;
		
		int pageNumber = 1;
		Map<Integer, String> studentNamesMap = new PdfPageStudentMapper(document).getPageNumberToStudentNameMap();
		for (PDPage page : document.getPages()) {
			
			try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
			                                                                 PDPageContentStream.AppendMode.APPEND,
			                                                                 true, false)) {
				contentStream.setFont(font, fontSize);
				float topY = page.getCropBox().getUpperRightY() - marginY;
				float leftX = page.getCropBox().getLowerLeftX() + marginX;
				float pageWidth = page.getCropBox().getWidth();
				
				// add an image
				PDImageXObject pdImage =
						PDImageXObject.createFromFile("src/main/resources/static/imgs/logo.jpg", document);
				int imageWidth = (int) (pageWidth / 20);
				int imageHeight = (int) (pageWidth / 20);
				contentStream.drawImage(pdImage, leftX, topY - imageHeight, imageWidth, imageHeight);
				
				// add some text next to the image
				contentStream.beginText();
				contentStream.newLineAtOffset(leftX + imageWidth + 10, topY - fontSize);
				contentStream.showText("Highland College");
				contentStream.endText();
				
				// align text to the right
				String studentName = studentNamesMap.getOrDefault(pageNumber, "");
				float textWidth = getTextWidth(font, fontSize, studentName);
				contentStream.beginText();
				contentStream.newLineAtOffset(pageWidth - marginX - textWidth, topY - fontSize);
				contentStream.showText(studentName);
				contentStream.endText();
				
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
			
			++pageNumber;
		}
		
		return new PDDocumentBuilder(document);
	}
	
	public PDDocumentBuilder addFooter() {
		log.info("Adding footer to PDF document");
		int fontSize = 12;
		PDType1Font font = PDType1Font.TIMES_ROMAN;
		
		float marginX = 40f;
		float marginY = 25f;
		
		String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		for (PDPage page : document.getPages()) {
			
			try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
			                                                                 PDPageContentStream.AppendMode.APPEND,
			                                                                 true, false)) {
				contentStream.setFont(font, fontSize);
				float bottomY = page.getCropBox().getLowerLeftY() + marginY;
				float leftX = page.getCropBox().getLowerLeftX() + marginX;
				
				contentStream.beginText();
				contentStream.newLineAtOffset(leftX, bottomY);
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
				float pageHeight = page.getMediaBox().getHeight();
				float pageWidth = page.getMediaBox().getWidth();
				
				double beta = Math.atan(pageHeight / pageWidth);
				double alpha = Math.toRadians(180.0) - beta - Math.toRadians(90);
				double a = textWidth * Math.sin(alpha);
				double b = textWidth * Math.cos(alpha);
				
				Matrix textMatrix = Matrix.getRotateInstance(beta,
				                                             (float) ((pageWidth - a) / 2),
				                                             (float) ((pageHeight - b) / 2) - (float) fontSize / 2);
				
				PDExtendedGraphicsState pdExtendedGraphicsState = new PDExtendedGraphicsState();
				pdExtendedGraphicsState.setNonStrokingAlphaConstant(0.5f);
				contentStream.setGraphicsStateParameters(pdExtendedGraphicsState);
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
