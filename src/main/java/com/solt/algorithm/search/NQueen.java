package com.solt.algorithm.search;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

/**
 * Solution for NQueen. This solution use Simulated annealing search. The
 * perfect configuration for this problem is patience = 200000
 * decrease = 0.95 temperature = 200
 * 
 * @author Dinh Trong Long
 * 
 */
public class NQueen {
	private static final String FILE_NAME = "src/main/resources/NQueen.properties";
	private List<Integer> bestQueen;
	private int denta;
	private int numQueen;
	private long patience;
	private float decrease;
	private double temp;

	/**
	 * load n queen problem configuration from file NQueen.properties
	 * 
	 * @throws IOException
	 */
	public void loadConf() throws IOException {
		Properties prop = new Properties();
		FileInputStream in = new FileInputStream(FILE_NAME);
		prop.load(in);
		numQueen = Integer.parseInt(prop.getProperty("numQueen", "100"));
		patience = Long.parseLong(prop.getProperty("patience", "100"));
		decrease = Float.parseFloat(prop.getProperty("decrease", "0.95"));
		temp = Double.parseDouble(prop.getProperty("temperature", "100"));
	}

	/**
	 * generate initial queen configuration for start search.
	 * @return initQueen
	 */
	public List<Integer> initQueen() {
		List<Integer> initQueen = new ArrayList<Integer>(numQueen);
		for (int i = 0; i < numQueen; i++) {
			initQueen.add(i, i);
		}
		return initQueen;
	}

	/**
	 * generate randomly the next configuration of the current.
	 * 
	 * @return nextQueen
	 */
	public List<Integer> getNextQueen(List<Integer> currentQueen) {
		// generate next configuration
		Random random = new Random();
		int startCol = random.nextInt(numQueen);
		int desCol;
		do {
			desCol = random.nextInt(numQueen);
		} while (desCol == startCol);
		
		int startRow = currentQueen.get(startCol);
		int desRow = currentQueen.get(desCol);

		// compute the denta of objective.
		denta = getObjLocal(startCol, desRow, desCol, startRow, currentQueen)
				- getObjLocal(startCol, startRow, desCol, desRow, currentQueen);
		
		List<Integer> nextQueen = new ArrayList<Integer>(currentQueen);
		nextQueen.set(startCol, desRow);
		nextQueen.set(desCol, startRow);
		return nextQueen;
	}

	/**
	 * get the number of pairs of queens that are attacking each other, either
	 * directly or indirectly with a given queen configuration that specified
	 * col and row of Queen.
	 * @param col1
	 * @param row1
	 * @param col2
	 * @param row2
	 * @param queenConf
	 * @return objLocal
	 */
	public int getObjLocal(int col1, int row1, int col2, int row2,
			List<Integer> queenConf) {
		int counter = 0;
		int row = 0;
		// compute for the queen at col1, row1.
		for (int i = 0; i < numQueen; ++i) {
			if (i == col1) {
				continue;
			}
			if (i == col2) {
				row = row2;
			} else {
				row = queenConf.get(i);
			}
			if ((col1 - i == row1 - row) || (col1 - i == row - row1)) {
				++counter;
			}
		}
		//compute for the queen at col2, row2.
		for (int i = 0; i < numQueen; ++i) {
			if (i == col2) {
				continue;
			}
			if (i == col1) {
				row = row1;
			} else {
				row = queenConf.get(i);
			}
			if ((col2 - i == row2 - row) || (col2 - i == row - row2)) {
				++counter;
			}
		}
		//check
		if ((col2 - col1 == row2 - row1) || (col2 - col1 == row1 -row2)) {
			--counter;
		}
		return counter;
	}

	/**
	 * perform find best Queen Configuration using Simulated annealing search.
	 * 
	 * @return best queen configuration.
	 */
	public List<Integer> findBestQueen() {
		List<Integer> currentQueen = initQueen();
		bestQueen = currentQueen;
		double rate = 0;

		// loop from 0 to patience to find
		for (int i = 0; i < patience; i++) {
			List<Integer> nextQueen = getNextQueen(currentQueen);

			if (denta <= 0) {
				// if objective of nextQueen is better than current
				currentQueen = nextQueen;
				bestQueen = nextQueen;
			} else if ((temp = decrease * temp) != 0) {
				// perform probability to assign nextQueen to current.

				rate = Math.exp(-denta / temp);
				if (Math.random() < rate) {
					currentQueen = nextQueen;
				}
			}
		}

		return bestQueen;
	}

	/**
	 * get number of pairs of queens that are attacking each other, either
	 * directly or indirectly.
	 * 
	 * @return number of pairs of queen that are attacking each other.
	 */
	public int getObjective() {
		int counter = 0;
		for (int i = 0; i < numQueen - 1; ++i) {
			for (int j = i + 1; j < numQueen; ++j) {
				if ((j - i == bestQueen.get(j) - bestQueen.get(i))
						|| (j - i == bestQueen.get(i) - bestQueen.get(j))
						|| (bestQueen.get(i) == bestQueen.get(j))) {
					++counter;
				}
			}
		}
		return counter;
	}

	/**
	 * Get the best queen configuration after invoke findBestQueen() method.
	 * 
	 * @return the best queen configuration.
	 */
	public List<Integer> getBestQuen() {
		return bestQueen;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("This Algorithm is running...");
		NQueen nQueen = new NQueen();
		nQueen.loadConf();
		List<Integer> bestQueen = nQueen.findBestQueen();
		int counter = nQueen.getObjective();
		if (counter == 0) {
			System.out.println("This Configuration is our goals");
		} else {
			System.out.println("This Configuration isn't our goals but it's "
					+ "the best we can find with Objective = " + counter);
		}
		
		for (int i : bestQueen) {
			System.out.print(i + " ");
		}
	}
}
