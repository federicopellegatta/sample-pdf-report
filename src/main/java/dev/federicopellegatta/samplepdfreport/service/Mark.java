package dev.federicopellegatta.samplepdfreport.service;

import lombok.Getter;

@Getter
public class Mark {
	private final float mark;
	private final boolean passed;
	
	public Mark(float mark) {
		this.mark = mark;
		this.passed = mark > 5.5f;
	}
	
	public Mark(double mark) {
		this.mark = (float) mark;
		this.passed = mark > 5.5f;
	}
	
	public float getMarkRounded(int places) {
		if (places < 0) throw new IllegalArgumentException("Places cannot be negative");
		
		double scale = Math.pow(10, places);
		return (float) (Math.round(mark * scale) / scale);
	}
	
}
