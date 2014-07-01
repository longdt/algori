package com.solt.algorithm.neuralnet.activation;

public class ActivationTANH implements ActivationFunction {

	@Override
	public double activationFunction(double d) {
		return (Math.exp(2 * d) - 1) / (Math.exp(2 * d) + 1);
	}

	@Override
	public double derivativeFunction(double d) {
		return 1 - d * d;
	}

}
