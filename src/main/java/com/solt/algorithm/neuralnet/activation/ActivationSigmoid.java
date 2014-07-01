package com.solt.algorithm.neuralnet.activation;

public class ActivationSigmoid implements ActivationFunction {

	@Override
	public double activationFunction(double d) {
		return 1.0 / (1 + Math.exp(-d));
	}

	@Override
	public double derivativeFunction(double d) {
		return d * (1 - d);
	}
	
}
