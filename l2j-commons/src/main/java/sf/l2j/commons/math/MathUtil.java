package sf.l2j.commons.math;

public class MathUtil {

	/**
	 * @param objectsSize : The overall elements size.
	 * @param pageSize : The number of elements per page.
	 * @return The number of pages, based on the number of elements and the
	 * number of elements we want per page.
	 */
	public static int countPagesNumber(int objectsSize, int pageSize) {
		return objectsSize / pageSize + (objectsSize % pageSize == 0 ? 0 : 1);
	}

	/**
	 * @param numToTest : The number to test.
	 * @param min : The minimum limit.
	 * @param max : The maximum limit.
	 * @return the number or one of the limit (mininum / maximum).
	 */
	public static int limit(int numToTest, int min, int max) {
		return (numToTest > max) ? max : ((numToTest < min) ? min : numToTest);
	}

	public static final double calculateAngleFrom(int obj1X, int obj1Y, int obj2X, int obj2Y) {
		double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		if (angleTarget < 0) {
			angleTarget = 360 + angleTarget;
		}

		return angleTarget;
	}

	public static final double convertHeadingToDegree(int clientHeading) {
		return clientHeading / 182.044444444;
	}

	public static final int convertDegreeToClientHeading(double degree) {
		if (degree < 0) {
			degree = 360 + degree;
		}

		return (int) (degree * 182.044444444);
	}

	public static final int calculateHeadingFrom(int obj1X, int obj1Y, int obj2X, int obj2Y) {
		double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		if (angleTarget < 0) {
			angleTarget = 360 + angleTarget;
		}

		return (int) (angleTarget * 182.044444444);
	}

	public static final int calculateHeadingFrom(double dx, double dy) {
		double angleTarget = Math.toDegrees(Math.atan2(dy, dx));
		if (angleTarget < 0) {
			angleTarget = 360 + angleTarget;
		}

		return (int) (angleTarget * 182.044444444);
	}

	public static double calculateDistance(int x1, int y1, int z1, int x2, int y2) {
		return calculateDistance(x1, y1, 0, x2, y2, 0, false);
	}

	public static double calculateDistance(int x1, int y1, int z1, int x2, int y2, int z2, boolean includeZAxis) {
		double dx = (double) x1 - x2;
		double dy = (double) y1 - y2;

		if (includeZAxis) {
			double dz = z1 - z2;
			return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
		}

		return Math.sqrt((dx * dx) + (dy * dy));
	}

	/**
	 * Returns the rounded value of val to specified number of digits after the
	 * decimal point.<BR>
	 * (Based on round() in PHP)
	 *
	 * @param val
	 * @param numPlaces
	 * @return float roundedVal
	 */
	public static float roundTo(float val, int numPlaces) {
		if (numPlaces <= 1) {
			return Math.round(val);
		}

		float exponent = (float) Math.pow(10, numPlaces);

		return (Math.round(val * exponent) / exponent);
	}
}
