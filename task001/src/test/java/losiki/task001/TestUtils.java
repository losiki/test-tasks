package losiki.task001;

import java.util.concurrent.atomic.AtomicLong;

public class TestUtils {
	private static final AtomicLong randomSuffix = new AtomicLong();
	
	public static String randomName() {
		return "name-"+randomSuffix.incrementAndGet();
	}
	
	public static String randomPosition() {
		return "position-"+randomSuffix.incrementAndGet();
	}

	public static String randomCity() {
		return "city-"+randomSuffix.incrementAndGet();
	}
}
