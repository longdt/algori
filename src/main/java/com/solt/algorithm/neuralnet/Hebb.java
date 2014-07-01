package com.solt.algorithm.neuralnet;

public class Hebb {
	private double w1;	//weight for neuron 1
	private double w2;	//weight for neuron 2
	private double rate = 1.0; //learning rate
	int epoch = 1;
	
	public Hebb() {
		w1= 1;
		w2 = -1;
	}
	
	protected void epoch() {
		System.out.println("***Beginning Epoch #" + epoch + "***");
		presentPattern(-1, -1);
		presentPattern(-1, 1);
		presentPattern(1, -1);
		presentPattern(1, 1);
		epoch++;
	}

	
	/**
	 * present pattern and learn
	 * @param i
	 * @param j
	 */
	private void presentPattern(double i1, double i2) {
		System.out.print("Presented [" + i1 + "," + i2 + "]");
		double result = recognize(i1, i2);
		System.out.print("result=" + result);
		
		//adjust w1;
		double delta = trainningFunction(rate, i1, result);
		System.out.print(", delta w1=" + delta);
		w1 += delta;
		delta = trainningFunction(rate, i2, result);
		System.out.println(", delta w2=" + delta);
		w2 += delta;
	}

	private double trainningFunction(double rate, double input, double output) {
		return rate * input * output;
	}

	public double recognize(double i1, double i2) {
		double a = w1 * i1 + w2 * i2;
		return a * 0.5;
	}
	
	public void run() {
		for (int i = 0; i < 5; ++i) {
			epoch();
		}
	}
	
	public static void main(String[] args) {
		Hebb h = new Hebb();
		h.run();
	}
	
}
