package com.solt.algorithm.neuralnet.feedback;

import java.util.ArrayList;
import java.util.List;

import com.solt.algorithm.neuralnet.NeuralNetworkError;
import com.solt.algorithm.neuralnet.feedback.FeedforwardLayer.LayerType;
import com.solt.algorithm.neuralnet.util.ErrorCalculation;
import com.solt.algorithm.neuralnet.util.MatrixCODEC;

public class FeedforwardNetwork {
	private FeedforwardLayer inputLayer;
	private List<FeedforwardLayer> layers = new ArrayList<FeedforwardLayer>();
	private FeedforwardLayer outputLayer;
	
	/**
	 * add layer
	 * @param feedforwardLayer
	 */
	public void addLayer(FeedforwardLayer feedforwardLayer) {
		if (inputLayer == null) {
			inputLayer = feedforwardLayer;
			inputLayer.setLayerType(LayerType.INPUT);
		}
		if (outputLayer != null) {
			outputLayer.connect(feedforwardLayer);
		}
		outputLayer = feedforwardLayer;
		layers.add(feedforwardLayer);
	}
	
	/**
	 * finalize structure of network
	 */
	public void finalizeStructure() {
		if (layers.size() < 2) {
			throw new NeuralNetworkError("FeedforwardNetwork must have least 2 layer");
		}
		outputLayer.setLayerType(LayerType.OUTPUT);
	}

	/**
	 * reset weight matrix and threshold/bias
	 */
	public void reset() {
		for (FeedforwardLayer layer : layers) {
			layer.reset();
		}
	}

	public double[] computeOutputs(double[] input) {
		if (input.length != inputLayer.getNeuronCount()) {
			throw new NeuralNetworkError("Size mismatch: can't compute outputs for input size="
					+ input.length
					+ " for input layer size=" + inputLayer.getNeuronCount());
		}
		for (FeedforwardLayer layer : layers) {
			if (layer.isInput()) {
				layer.computeOutputs(input);
			} else if (layer.isHidden()) {
				layer.computeOutputs(null);
			}
		}
		return outputLayer.getFire();
	}

	public double calculateError(double[][] input, double[][] ideal) {
		ErrorCalculation errCal = new ErrorCalculation();
		for (int i = 0; i < input.length; ++i) {
			double[] output = computeOutputs(input[i]);
			errCal.updateError(output, ideal[i]);;
		}
		return errCal.calculateRMS();
	}
	
	@Override
	public Object clone() {
		FeedforwardNetwork result = cloneStructure();
		final Double[] data = MatrixCODEC.networkToArray(this);
		MatrixCODEC.arrayToNetwork(data, result);
		return result;
	}

	public FeedforwardNetwork cloneStructure() {
		FeedforwardNetwork result = new FeedforwardNetwork();
		for (FeedforwardLayer layer : layers) {
			FeedforwardLayer cloneLayer = new FeedforwardLayer(layer.getNeuronCount());
			result.addLayer(cloneLayer);
		}
		result.finalizeStructure();
		return result;
	}
	
	public FeedforwardLayer getOutputLayer() {
		return outputLayer;
	}

	public List<FeedforwardLayer> getLayers() {
		return layers;
	}

	public int getWeightMatrixSize() {
		int result = 0;
		for (final FeedforwardLayer layer : this.layers) {
			result += layer.getMatrixSize();
		}
		return result;
	}

}
