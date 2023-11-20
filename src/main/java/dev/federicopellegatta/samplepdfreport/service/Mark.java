package dev.federicopellegatta.samplepdfreport.service;

import dev.federicopellegatta.samplepdfreport.utils.MathUtils;
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
		return MathUtils.round(mark, places);
	}
	
}
