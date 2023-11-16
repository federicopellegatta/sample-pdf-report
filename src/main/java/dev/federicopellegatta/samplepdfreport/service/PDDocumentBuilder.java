package dev.federicopellegatta.samplepdfreport.service;

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

public class PDDocumentBuilder {
	private final PDDocument document;
	
	public PDDocumentBuilder(PDDocument document) {
		this.document = document;
	}
	
	public PDDocumentBuilder addHeader() {
		int fontSize = 15;
		PDType1Font font = PDType1Font.TIMES_ROMAN;
		
		for (PDPage page : document.getPages()) {
			
			try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
			                                                                 PDPageContentStream.AppendMode.APPEND,
			                                                                 true, false)) {
				contentStream.setFont(font, fontSize);
				float startY = page.getCropBox().getUpperRightY() - 30;
				float startX = page.getCropBox().getLowerLeftX() + 55;
				
				PDImageXObject pdImage =
						PDImageXObject.createFromFile("src/main/resources/static/imgs/logo.jpg", document);
				int imageWidth = (int) (page.getCropBox().getWidth() / 20);
				int imageHeight = (int) (page.getCropBox().getWidth() / 20);
				contentStream.drawImage(pdImage, startX, startY - imageHeight, imageWidth, imageHeight);
				
				
				contentStream.beginText();
				contentStream.newLineAtOffset(startX + imageWidth + 10, startY - fontSize);
				contentStream.showText("School Report");
				
				contentStream.endText(); // End of text mode
				
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		return new PDDocumentBuilder(document);
	}
	
	public PDDocumentBuilder addFooter() {
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