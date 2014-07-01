package com.solt.algorithm.neuralnet.activation;

import com.solt.algorithm.neuralnet.NeuralNetworkError;

public class ActivationLinear implements ActivationFunction {

	@Override
	public double activationFunction(double d) {
		return d;
	}

	@Override
	public double derivativeFunction(double d) {
		throw new NeuralNetworkError("Can't use linear activation when derivative function is required");
	}

}
