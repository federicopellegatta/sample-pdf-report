package dev.federicopellegatta.samplepdfreport.utils;

public class MathUtils {
	private MathUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static <T extends Number> float round(T value, int places) {
		if (places < 0) throw new IllegalArgumentException("Places cannot be negative");
		
		float scale = (float) Math.pow(10, places);
		return Math.round((float) value * scale) / scale;
	}
}
