package com.solt.algorithm.math;

public class BiPolarUtil {

	public static double bipolar2Double(boolean b) {
		if (b) {
			return 1;
		} else {
			return -1;
		}
	}

	public static double[] bipolar2Double(boolean b[]) {
		final double[] result = new double[b.length];

		for (int i = 0; i < b.length; i++) {
			result[i] = bipolar2Double(b[i]);
		}

		return result;
	}

	public static double[][] bipolar2Double(boolean b[][]) {
		final double[][] result = new double[b.length][b[0].length];

		for (int row = 0; row < b.length; row++) {
			for (int col = 0; col < b[0].length; col++) {
				result[row][col] = bipolar2Double(b[row][col]);
			}
		}

		return result;
	}

	public static boolean double2Bipolar(double d) {
		if (d > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean[] double2Bipolar(double d[]) {
		final boolean[] result = new boolean[d.length];

		for (int i = 0; i < d.length; i++) {
			result[i] = double2Bipolar(d[i]);
		}

		return result;
	}

	public static boolean[][] double2Bipolar(double d[][]) {
		final boolean[][] result = new boolean[d.length][d[0].length];

		for (int row = 0; row < d.length; row++) {
			for (int col = 0; col < d[0].length; col++) {
				result[row][col] = double2Bipolar(d[row][col]);
			}
		}

		return result;
	}

	public static double normalizeBinary(double d) {
		if (d > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	public static double toBinary(double d) {
		return (d + 1) / 2.0;
	}

	public static double toBiPolar(double d) {
		return (2 * normalizeBinary(d)) - 1;
	}

	public static double toNormalizedBinary(double d) {
		return normalizeBinary(toBinary(d));
	}
}
