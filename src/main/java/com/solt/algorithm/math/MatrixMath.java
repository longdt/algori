package com.solt.algorithm.math;

public class MatrixMath {
	public static Matrix add(Matrix a, Matrix b) {
		if (a.getRows() != b.getRows()) {
			throw new MatrixError(
					"To add the matrices they must have the same number of rows and columns.  Matrix a has "
							+ a.getRows()
							+ " rows and matrix b has "
							+ b.getRows() + " rows.");
		}

		if (a.getCols() != b.getCols()) {
			throw new MatrixError(
					"To add the matrices they must have the same number of rows and columns.  Matrix a has "
							+ a.getCols()
							+ " cols and matrix b has "
							+ b.getCols() + " cols.");
		}
		
		double[][] mData = new double[a.getRows()][a.getCols()];
		for (int i = 0; i < a.getRows(); ++i) {
			for (int j = 0; j < a.getCols(); ++j) {
				mData[i][j] = a.get(i, j) + b.get(i, j);
			}
		}
		return Matrix.wrap(mData);
	}
	
	public static Matrix divide(Matrix a, double b) {
		double[][] mData = new double[a.getRows()][a.getCols()];
		for (int i = 0; i < a.getRows(); ++i) {
			for (int j = 0; j < a.getCols(); ++j) {
				mData[i][j] = a.get(i, j) / b;
			}
		}
		return Matrix.wrap(mData);
	}
	
	public static double doProduct(Matrix a, Matrix b) {
		if (!a.isVector() || !b.isVector()) {
			throw new MatrixError("two matrices must be vectors.");
		}
		double[] arrayA = a.toPackedArray();
		double[] arrayB = b.toPackedArray();
		if (arrayA.length != arrayB.length) {
			throw new MatrixError("two matrices must have same size");
		}
		double val = 0;
		for (int i = 0; i < arrayA.length; ++i) {
			val += arrayA[i] * arrayB[i];
		}
		return val;
	}
	
	public static Matrix identity(int size) {
		if (size < 1) {
			throw new MatrixError("Identity matrix must be at least of size 1.");
		}
		Matrix m = new Matrix(size, size);
		for (int i = 0; i < size; ++i) {
			m.set(i, i, 1);
		}
		return m;
	}
	
	public static Matrix multiply(Matrix a, double b) {
		double[][] mData = new double[a.getRows()][a.getCols()];
		for (int i = 0; i < a.getRows(); ++i) {
			for (int j = 0; j < a.getCols(); ++j) {
				mData[i][j] = a.get(i, j) * b;
			}
		}
		return Matrix.wrap(mData);
	}
	
	public static Matrix multiply(Matrix a, Matrix b) {
		if (a.getCols() != b.getRows()) {
			throw new MatrixError(
					"To use ordinary matrix multiplication the number of columns on the first matrix must mat the number of rows on the second.");
		}
		double[][] mData = new double[a.getRows()][b.getCols()];
		for (int resultRow = 0; resultRow < a.getRows(); ++resultRow) {
			for (int resultCol = 0; resultCol < b.getCols(); ++resultCol) {
				double val = 0;
				for (int aCol = 0; aCol < a.getCols(); ++aCol) {
					val += a.get(resultRow, aCol) * b.get(aCol, resultCol);
				}
				mData[resultRow][resultCol] = val;
			}
		}
		return Matrix.wrap(mData);
	}
	
	public static Matrix subtract(Matrix a, Matrix b) {
		if (a.getRows() != b.getRows()) {
			throw new MatrixError(
					"To add the matrices they must have the same number of rows and columns.  Matrix a has "
							+ a.getRows()
							+ " rows and matrix b has "
							+ b.getRows() + " rows.");
		}

		if (a.getCols() != b.getCols()) {
			throw new MatrixError(
					"To add the matrices they must have the same number of rows and columns.  Matrix a has "
							+ a.getCols()
							+ " cols and matrix b has "
							+ b.getCols() + " cols.");
		}
		
		double[][] mData = new double[a.getRows()][a.getCols()];
		for (int i = 0; i < a.getRows(); ++i) {
			for (int j = 0; j < a.getCols(); ++j) {
				mData[i][j] = a.get(i, j) - b.get(i, j);
			}
		}
		return Matrix.wrap(mData);
	}
	
	public static Matrix transpose(Matrix matrix) {
		double[][] mData = new double[matrix.getCols()][matrix.getRows()];
		for (int i = 0; i < matrix.getRows(); ++i) {
			for (int j = 0; j < matrix.getCols(); ++j) {
				mData[j][i] = matrix.get(i, j);
			}
		}
		return Matrix.wrap(mData);
	}
	
	public static double vectorLength(Matrix matrix) {
		if (!matrix.isVector()) {
			throw new MatrixError(
					"Can only take the vector length of a vector.");
		}
		final double v[] = matrix.toPackedArray();
		double rtn = 0.0;
		for (int i = 0; i < v.length; i++) {
			rtn += Math.pow(v[i], 2);
		}
		return Math.sqrt(rtn);
	}
}
