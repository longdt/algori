package com.solt.algorithm.neuralnet.util;

public class BoundNumbers {
	public static final double TOO_SMALL = -1.0E20;
	
	public static final double TOO_BIG = 1.0E20;

	public static double bound(double number) {
		if (number < TOO_SMALL) {
			return TOO_SMALL;
		} else if (number > TOO_BIG) {
			return TOO_BIG;
		} else {
			return number;
		}
	}

}
