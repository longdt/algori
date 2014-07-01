package com.solt.algorithm.neuralnet.feedback.train.backpropagation;

import com.solt.algorithm.math.Matrix;
import com.solt.algorithm.math.MatrixMath;
import com.solt.algorithm.neuralnet.feedback.FeedforwardLayer;
import com.solt.algorithm.neuralnet.util.BoundNumbers;

public class BackpropagationLayer {
	private final double[] error;
	private final double[] errorDelta;
	private int biasRow;
	private Matrix accMatrixDelta;
	private Matrix matrixDelta;
	private Backpropagation backpropagation;
	private FeedforwardLayer layer;

	
	public BackpropagationLayer(Backpropagation backpropagation, FeedforwardLayer layer) {
		this.backpropagation = backpropagation;
		this.layer = layer;
		int neuronCnt = layer.getNeuronCount();
		error = new double[neuronCnt];
		errorDelta = new double[neuronCnt];
		if (layer.getNext() != null) {
			accMatrixDelta = new Matrix(layer.getNeuronCount() + 1, layer.getNext().getNeuronCount());
			matrixDelta = new Matrix(layer.getNeuronCount() + 1, layer.getNext().getNeuronCount());
			biasRow = layer.getNeuronCount();
		}
	}
	
	
	public void clearError() {
		for (int i = 0; i < error.length; ++i) {
			error[i] = 0;
		}
	}

	public void calcError(double[] ideal) {
		for (int i = 0; i < layer.getNeuronCount(); ++i) {
			setError(i, ideal[i] - layer.getFire(i));
			setErrorDelta(i, BoundNumbers.bound(calculateDelta(i)));
		}
	}

	private void setErrorDelta(int i, double errorDelta) {
		this.errorDelta[i] = errorDelta;
	}
	
	private double getErrorDelta(int i) {
		return errorDelta[i];
	}

	private double calculateDelta(int i) {
		return error[i] * layer.getActivationFunction().derivativeFunction(layer.getFire(i));
	}

	private void setError(int i, double err) {
		error[i] = BoundNumbers.bound(err);
	}
	
	private double getError(int i) {
		return error[i];
	}

	public void calcError() {
		BackpropagationLayer next = backpropagation.getBackpropagationLayer(layer.getNext());
		for (int i = 0; i < layer.getNext().getNeuronCount(); ++i) {
			for (int j = 0; j < layer.getNeuronCount(); ++j) {
				accumulateMatrixDelta(j, i, next.getErrorDelta(i) * layer.getFire(j));
				setError(j, getError(j) + layer.getMatrix().get(j, i) * next.getErrorDelta(i));
			}
			accumulateBiasDelta(i, next.getErrorDelta(i));
		}
		if (layer.isHidden()) {
			for (int i = 0; i < layer.getNeuronCount(); ++i) {
				setErrorDelta(i, BoundNumbers.bound(calculateDelta(i)));
			}
		}
	}

	private void accumulateBiasDelta(int index, double value) {
		accMatrixDelta.add(biasRow, index, value);
	}


	private void accumulateMatrixDelta(int i1, int i2, double value) {
		accMatrixDelta.add(i1, i2, value);
	}


	public void learn(double learnRate, double momentum) {
		if (layer.hasMatrix()) {
			Matrix m1 = MatrixMath.multiply(accMatrixDelta, learnRate);
			Matrix m2 = MatrixMath.multiply(matrixDelta, momentum);
			matrixDelta = MatrixMath.add(m1, m2);
			layer.setMatrix(MatrixMath.add(layer.getMatrix(), matrixDelta));
			accMatrixDelta.clear();
		}
	}

}
