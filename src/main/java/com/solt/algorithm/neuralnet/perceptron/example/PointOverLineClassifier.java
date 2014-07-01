package com.solt.algorithm.neuralnet.perceptron.example;

import java.util.Random;

import com.solt.algorithm.neuralnet.perceptron.Perceptron;

public class PointOverLineClassifier {
	private static final Random RAND = new Random(System.currentTimeMillis());
	private Perceptron perceptron;
	private int width;
	private int height;
	
	public PointOverLineClassifier() {
		perceptron = new Perceptron(3);
		width = 480;
		height = 360;
		setup();
	}

	private void setup() {
		for (int i = 0; i < 2000; ++i) {
			int x = RAND.nextInt(width);
			int y = RAND.nextInt(height);
			int answer = y > yLine(x) ? 1 : -1;
			perceptron.train(new float[] {x, y , 1}, answer);
		}
	}
	
	public int yLine(int x) {
		return 2 * x + 1;
	}
	
	public int guess(int x, int y) {
		return perceptron.feedForward(new float[] {x, y, 1});
	}

	public static void main(String[] args) {
		PointOverLineClassifier classifer = new PointOverLineClassifier();
		for (int i = 0; i < 20; ++i) {
			int x = RAND.nextInt(classifer.width);
			int y = RAND.nextInt(classifer.height);
			int answer = y > classifer.yLine(x) ? 1 : -1;
			int guess = classifer.guess(x, y);
			System.out.println("classify (" + x + ", " + y + ") is " + guess + " (" + (answer - guess) + ")");
		}
	}

}
