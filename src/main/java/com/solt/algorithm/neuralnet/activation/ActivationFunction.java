package com.solt.algorithm.neuralnet.activation;

public interface ActivationFunction {
	
	public double activationFunction(double d);
	
	/**
	 * d is value of {@link activationFunction}
	 * @param d
	 * @return
	 */
	public double derivativeFunction(double d);
}
