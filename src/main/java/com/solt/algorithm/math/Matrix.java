package com.solt.algorithm.math;

import java.util.Arrays;

public class Matrix {
	private int rows;
	private int cols;
	private double[][] cells;
	
	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		cells = new double[rows][cols];
	}
	
	public Matrix(double[][] mData) {
		this(mData, true);
	}
	
	Matrix(double[][] mData, boolean copy) {
		rows = mData.length;
		cols = mData[0].length;
		if (copy) {
			cells = new double[rows][];
			for (int i = 0; i < rows; ++i) {
				cells[i] = Arrays.copyOf(mData[i], cols);
			}
		} else {
			cells = mData;
		}
	}
	
	public static Matrix createRowMatrix(double[] mData) {
		Matrix m = new Matrix(new double[][] {mData}, false);
		return m;
	}
	
	public static Matrix createColMatrix(double[] mData) {
		Matrix m = new Matrix(mData.length, 1);
		for (int i = 0; i < mData.length; ++i) {
			m.cells[i][0] = mData[i];
		}
		return m;
	}
	
	public static Matrix wrap(double[][] mData) {
		return new Matrix(mData, false);
	}
	
	public void add(int row, int col, double val) {
		cells[row][col] += val;
	}
	
	public void clear() {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				cells[i][j] = 0;
			}
		}
	}
	
	public Matrix clone() {
		return new Matrix(cells);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Matrix)) {
			return false;
		}
		Matrix matrix = (Matrix) obj;
		if (rows != matrix.rows || cols != matrix.cols) {
			return false;
		}
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				if (cells[i][j] != matrix.cells[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean equals(Matrix matrix, int precision) {
		if (precision < 0) {
			throw new MatrixError("Precision can't be a nagative number");
		}
		double test = Math.pow(10.0, precision);
		if (Double.isInfinite(test) || test > Long.MAX_VALUE) {
			throw new MatrixError("Precision of " + precision + " decimal places is not support");
		}
		precision = (int) Math.pow(10, precision);
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				if ((long) (cells[i][j] * precision) != (long) (matrix.cells[i][j])) {
					return false;
				}
			}
		}
		return true;
	}
	
	public double get(int row, int col) {
		return cells[row][col];
	}
	
	public Matrix getCol(int col) {
		if (col < 0 || col >= cols) {
			throw new MatrixError("invalid col index");
		}
		Matrix m = new Matrix(rows, 1);
		for (int i = 0; i < rows; ++i) {
			m.cells[i][0] = cells[i][col];
		}
		return m;
	}

	public int getRows() {
		return rows;
	}
	
	public Matrix getRow(int row) {
		if (row < 0 || row >= rows) {
			throw new MatrixError("invalid row index");
		}
		return createRowMatrix(cells[row]);
	}

	public int getCols() {
		return cols;
	}
	
	public boolean isVector() {
		return rows == 1 || cols == 1;
	}
	
	public boolean isZero() {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				if (cells[i][j] != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void set(int row, int col, double val) {
		cells[row][col] = val;
	}
	
	public double sum() {
		double val = 0;
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				val += cells[i][j];
			}
		}
		return val;
	}
	
	public double[] toPackedArray() {
		double[] result = new double[rows * cols];
		for (int i = 0; i < rows; ++i) {
			System.arraycopy(cells[i], 0, result, i * cols, cols);
		}
		return result;
	}

	public void randomize(double min, double max) {
		double scale = max - min;
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				cells[i][j] = Math.random() * scale + min;
			}
		}
	}
}
