package com.solt.algorithm.neuralnet.feedback.example;

import com.solt.algorithm.neuralnet.feedback.FeedforwardLayer;
import com.solt.algorithm.neuralnet.feedback.FeedforwardNetwork;
import com.solt.algorithm.neuralnet.feedback.train.Train;
import com.solt.algorithm.neuralnet.feedback.train.backpropagation.Backpropagation;

public class XOR {
	public static double XOR_INPUT[][] = { { 0, 0 }, { 1, 0 }, { 0, 1 },
			{ 1, 1 } };

	public static double XOR_IDEAL[][] = { { 0 }, { 1 }, { 1 }, { 0 } };

	public static void main(String[] args) {
		FeedforwardNetwork network = new FeedforwardNetwork();
		network.addLayer(new FeedforwardLayer(2));
		network.addLayer(new FeedforwardLayer(3));
		network.addLayer(new FeedforwardLayer(1));
		network.finalizeStructure();
		network.reset();

		Train train = new Backpropagation(network, XOR_INPUT, XOR_IDEAL, 0.7,
				0.9);
		int epoch = 1;
		do {
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error: "
					+ train.getError());
			++epoch;
		} while (epoch < 50000 && (train.getError() > 0.001));

		// test the neural network
		for (int i = 0; i < XOR_IDEAL.length; ++i) {
			double actual[] = network.computeOutputs(XOR_INPUT[i]);
			System.out.println(XOR_INPUT[i][0] + "," + XOR_INPUT[i][1]
					+ ", actual=" + actual[0] + ", ideal=" + XOR_IDEAL[i][0]);
		}

	}

}
