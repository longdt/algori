package com.solt.algorithm.neuralnet.hopfield;

import com.solt.algorithm.math.BiPolarUtil;
import com.solt.algorithm.math.Matrix;
import com.solt.algorithm.math.MatrixMath;
import com.solt.algorithm.neuralnet.NeuralNetworkError;

public class HopfieldNetwork {
	private Matrix weightMatrix;
	
	public HopfieldNetwork(int size) {
		weightMatrix = new Matrix(size, size);
	}
	
	public Matrix getMatrix() {
		return weightMatrix;
	}
	
	public int getSize() {
		return weightMatrix.getRows();
	}
	
	public boolean[] present(boolean[] pattern) {
		boolean[] output = new boolean[pattern.length];
		//convert input pattern -> bipolar matrix with a single row
		Matrix inputMatrix = Matrix.createRowMatrix(BiPolarUtil.bipolar2Double(pattern));
		for (int col = 0; col < pattern.length; ++col) {
			Matrix colMatrix = weightMatrix.getCol(col);
			colMatrix = MatrixMath.transpose(colMatrix);
			double dotProduct = MatrixMath.doProduct(inputMatrix, colMatrix);
			output[col] = dotProduct > 0 ? true : false;
		}
		return output;
	}
	
	public void train(boolean[] pattern) {
		if (pattern.length != weightMatrix.getRows()) {
			throw new NeuralNetworkError("Can't train a pattern of size " + pattern.length
					+ " on a hopfield network of size " + weightMatrix.getRows());
		}
		
		Matrix m2 = Matrix.createRowMatrix(BiPolarUtil.bipolar2Double(pattern));
		Matrix m1 = MatrixMath.transpose(m2);
		Matrix m3 = MatrixMath.multiply(m1, m2);
		
		Matrix identity = MatrixMath.identity(pattern.length);
		Matrix m4 = MatrixMath.subtract(m3, identity);
		weightMatrix = MatrixMath.add(weightMatrix, m4);
	}
}
