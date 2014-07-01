package com.solt.algorithm.neuralnet.feedback.train.backpropagation;

import java.util.HashMap;
import java.util.Map;

import com.solt.algorithm.neuralnet.NeuralNetworkError;
import com.solt.algorithm.neuralnet.feedback.FeedforwardLayer;
import com.solt.algorithm.neuralnet.feedback.FeedforwardNetwork;
import com.solt.algorithm.neuralnet.feedback.train.Train;

public class Backpropagation implements Train {
	private FeedforwardNetwork network;
	private Map<FeedforwardLayer, BackpropagationLayer> backpropLayers;
	private double[][] input;
	private double[][] ideal;
	private double learnRate;
	private double momentum;
	private double error;
	

	public Backpropagation(FeedforwardNetwork network, double[][] input,
			double[][] ideal, double learnRate, double momentum) {
		this.network = network;
		this.input = input;
		this.ideal = ideal;
		this.learnRate = learnRate;
		this.momentum = momentum;
		backpropLayers = new HashMap<FeedforwardLayer, BackpropagationLayer>();
	}

	@Override
	public void iteration() {
		for (int i = 0; i < input.length; ++i) {
			//present pattern to network and output was calculated
			network.computeOutputs(input[i]);
			//calculate error by compare output to ideal value
			calcError(ideal[i]);
			//learn/modify weights
			learn();
		}
		error = network.calculateError(input, ideal);
	}

	private void learn() {
		for (FeedforwardLayer layer : network.getLayers()) {
			getBackpropagationLayer(layer).learn(learnRate, momentum);
		}
	}

	private void calcError(double[] ideal) {
		if (ideal.length != network.getOutputLayer().getNeuronCount()) {
			throw new NeuralNetworkError("Size mismatch: can't calcError for ideal input size="
					+ ideal.length + " for output layer size=" + network.getOutputLayer().getNeuronCount());
		}
		//clear previous errors of BackpropagationLayer
		for (FeedforwardLayer layer : network.getLayers()) {
			getBackpropagationLayer(layer).clearError();
		}
		//propagates backwards
		for (int i = network.getLayers().size() - 1; i >= 0; --i) {
			FeedforwardLayer layer = network.getLayers().get(i);
			if (layer.isOutput()) {
				getBackpropagationLayer(layer).calcError(ideal);
			} else {
				getBackpropagationLayer(layer).calcError();
			}
		}
	}

	public BackpropagationLayer getBackpropagationLayer(FeedforwardLayer layer) {
		BackpropagationLayer backLayer = backpropLayers.get(layer);
		if (backLayer == null) {
			backLayer = new BackpropagationLayer(this, layer);
			backpropLayers.put(layer, backLayer);
		}
		return backLayer;
	}

	@Override
	public double getError() {
		return error;
	}

	@Override
	public FeedforwardNetwork getNetwork() {
		return network;
	}

}
