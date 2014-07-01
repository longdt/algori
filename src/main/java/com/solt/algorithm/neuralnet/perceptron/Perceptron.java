package com.solt.algorithm.neuralnet.perceptron;

import java.util.Random;

public class Perceptron {
	private float c = 0.01f;
	private Random rand = new Random(System.currentTimeMillis());
	private float[] weights;
	
	public Perceptron(int n) {
		weights = new float[n];
		for (int i = 0; i < n; ++i) {
			weights[i] = rand.nextFloat();
			if (rand.nextBoolean()) {
				weights[i] = 0 - weights[i];
			}
		}
	}
	
	public int feedForward(float[] inputs) {
		float sum = 0;
		for (int i = 0; i < weights.length; ++i) {
			sum += inputs[i] * weights[i];
		}
		return activate(sum);
	}

	private int activate(float sum) {
		return sum > 0 ? 1: -1;
	}
	
	public void train(float[] inputs, int desired) {
		int guess = feedForward(inputs);
		float error = desired - guess;
		for (int i = 0; i < weights.length; ++i) {
			weights[i] += c * error * inputs[i];
		}
	}
}
