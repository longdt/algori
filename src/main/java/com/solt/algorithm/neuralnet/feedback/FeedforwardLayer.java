package com.solt.algorithm.neuralnet.feedback;

import com.solt.algorithm.math.Matrix;
import com.solt.algorithm.math.MatrixMath;
import com.solt.algorithm.neuralnet.activation.ActivationFunction;
import com.solt.algorithm.neuralnet.activation.ActivationSigmoid;

public class FeedforwardLayer {
	static enum LayerType {INPUT, HIDDEN, OUTPUT};
	private static final ActivationFunction DEFAULT_ACTIVATION = new ActivationSigmoid();
	private ActivationFunction activationFunc;
	private int neurons;
	private LayerType type = LayerType.HIDDEN;
	private double[] fire;
	private FeedforwardLayer next;
	private Matrix matrix;
	
	public FeedforwardLayer(int neurons) {
		this(DEFAULT_ACTIVATION, neurons);
	}
	
	public FeedforwardLayer(ActivationFunction activationFunc, int neurons) {
		this.activationFunc = activationFunc;
		this.neurons = neurons;
		fire = new double[neurons];
	}

	public int getNeuronCount() {
		return neurons;
	}
	
	void setLayerType(LayerType type) {
		this.type = type;
	}
	
	void connect(FeedforwardLayer layer) {
		this.next = layer;
		matrix = new Matrix(neurons + 1, next.getNeuronCount());
	}

	public boolean isInput() {
		return type == LayerType.INPUT;
	}
	
	public boolean isHidden() {
		return type == LayerType.HIDDEN;
	}

	public double[] computeOutputs(double[] pattern) {
		if (pattern != null) {
			for (int i = 0; i < getNeuronCount(); ++i) {
				setFire(i, pattern[i]);
			}
		}
		Matrix inputMatrix = createInputMatrix(fire);
		for (int i = 0; i < next.getNeuronCount(); ++i) {
			Matrix col = matrix.getCol(i);
			double sum = MatrixMath.doProduct(inputMatrix, col);
			next.setFire(i, activationFunc.activationFunction(sum));
		}
		return fire;
	}

	private Matrix createInputMatrix(double[] input) {
		Matrix result = new Matrix(1, input.length + 1);
		for (int i = 0; i < input.length; ++i) {
			result.set(0, i, input[i]);
		}
		result.set(0, input.length, 1);
		return result;
	}

	private void setFire(int i, double d) {
		fire[i] = d;
	}



	public double[] getFire() {
		return fire;
	}

	public boolean isOutput() {
		return type == LayerType.OUTPUT;
	}

	public double getFire(int i) {
		return fire[i];
	}

	public FeedforwardLayer getNext() {
		return next;
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public boolean hasMatrix() {
		return matrix != null;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
	
	public ActivationFunction getActivationFunction() {
		return activationFunc;
	}

	public void reset() {
		if (matrix != null) {
			matrix.randomize(-1, 1);
		}
	}

}
