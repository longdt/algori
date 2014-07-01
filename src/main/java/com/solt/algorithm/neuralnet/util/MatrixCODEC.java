package com.solt.algorithm.neuralnet.util;

import com.solt.algorithm.neuralnet.feedback.FeedforwardLayer;
import com.solt.algorithm.neuralnet.feedback.FeedforwardNetwork;

public class MatrixCODEC {

	public static void arrayToNetwork(Double[] array, FeedforwardNetwork network) {
		int index = 0;
		for (FeedforwardLayer layer : network.getLayers()) {
			if (layer.hasMatrix()) {
				index = layer.getMatrix().fromPackedArray(array, index);
			}
		}
	}
	
	public static void arrayToNetwork(double[] array, FeedforwardNetwork network) {
		int index = 0;
		for (FeedforwardLayer layer : network.getLayers()) {
			if (layer.hasMatrix()) {
				index = layer.getMatrix().fromPackedArray(array, index);
			}
		}
	}

	public static Double[] networkToArray(FeedforwardNetwork network) {
		int size = 0;
		for (FeedforwardLayer layer : network.getLayers()) {
			if (layer.hasMatrix()) {
				size += layer.getMatrixSize();
			}
		}
		
		Double[] result = new Double[size];
		int index = 0;
		for (FeedforwardLayer layer : network.getLayers()) {
			if (layer.hasMatrix()) {
				size += layer.getMatrixSize();
				double[] matrix = layer.getMatrix().toPackedArray();
				for (int i = 0; i< matrix.length; ++i) {
					result[index++] = matrix[i];
				}
			}
		}
		return result;
	}

}
