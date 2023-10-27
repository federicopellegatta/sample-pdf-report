package dev.federicopellegatta.samplepdfreport.component;

import dev.federicopellegatta.samplepdfreport.entity.Subject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
public class RandomComponent {
	private final Random random = new Random();
	
	public boolean bool() {
		return random.nextBoolean();
	}
	
	public LocalDate localDate() {
		return localDate(1970);
	}
	
	public LocalDate localDate(int minYear) {
		LocalDate start = LocalDate.of(minYear, Month.JANUARY, 1);
		long days = ChronoUnit.DAYS.between(start, LocalDate.now());
		return start.plusDays(random.nextInt((int) days + 1));
	}
	
	public int integer(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	public int integer(int max) {
		return integer(0, max);
	}
	
	public float gaussian(float mean, float std) {
		return (float) (random.nextGaussian() * std + mean);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T enumValue(Class<? extends Enum<T>> cls) {
		Enum<T>[] enumConstants = cls.getEnumConstants();
		return (T) Arrays.asList(enumConstants).get(integer(enumConstants.length - 1));
	}
}
