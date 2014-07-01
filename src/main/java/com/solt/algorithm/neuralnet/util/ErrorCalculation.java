package com.solt.algorithm.neuralnet.util;

public class ErrorCalculation {
	private double globalError;
	private int setSize;
	
	public double calculateRMS() {
		return Math.sqrt(globalError / setSize);
	}
	
	public void reset() {
		globalError = 0;
		setSize = 0;
	}
	
	public void updateError(double[] actual, double[] ideal) {	
		for (int i = 0; i < actual.length; ++i) {
			double delta = ideal[i] - actual[i];
			globalError += delta * delta;
		}
		setSize += ideal.length;
	}
}
