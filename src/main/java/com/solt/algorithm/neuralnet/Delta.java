package com.solt.algorithm.neuralnet;

public class Delta {
	private double w1;
	private double w2;
	private double w3;
	private double rate = 0.5;
	private int epoch = 1;
	
	public Delta() {
	}
	
	private void epoch() {
		System.out.println("***Beginning Epoch #" + epoch + "***");
		presentPattern(0, 0, 1, 0);
		presentPattern(0, 1, 1, 0);
		presentPattern(1, 0, 1, 0);
		presentPattern(1, 1, 1, 1);
		++epoch;
	}
	
	private double getError(double actual, double anticipated) {
		return (anticipated - actual);
	}
	
	private void presentPattern(double i1, double i2, double i3, double anticipated) {
		System.out.print("Present [" + i1 + "," + i2 + "," + i3 + "]");
		double actual = recognize(i1, i2, i3);
		double err = getError(actual, anticipated);
		System.out.println(" anticipated=" + anticipated + " actual=" + actual + " error=" + err);
		double delta = trainingFunction(rate, i1, err);
		w1 += delta;
		delta = trainingFunction(rate, i2, err);
		w2 += delta;
		delta = trainingFunction(rate, i3, err);
		w3 += delta;
	}

	private double trainingFunction(double rate, double input, double err) {
		return rate * input * err;
	}

	public double recognize(double i1, double i2, double i3) {
		return (w1 * i1 + w2 * i2 + w3 * i3) * 0.5;
	}

	public void run() {
		for (int i = 0; i < 1000; ++i) {
			epoch();
		}
	}
	
	public static void main(String[] args) {
		Delta delta = new Delta();
		delta.run();
	}
}
