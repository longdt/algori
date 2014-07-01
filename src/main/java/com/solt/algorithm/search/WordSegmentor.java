/**
 * 
 */
package com.solt.algorithm.search;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.solt.sql.Connector;

/**
 * @author thienlong
 * 
 */
public class WordSegmentor {
	private static final String CONFIG_FILE = "";
	private int numLoop;
	private int populationSize;
	private double crossOverRate;
	private double mutationRate;
	private Map<String, Double> vocab;
	private double[] percentWordLength = { 14.2, 68.1, 8.0, 9.7 };
	private double thresdhold;
	private Logger logger = Logger.getLogger(WordSegmentor.class);

	public WordSegmentor() {
		vocab = new HashMap<String, Double>();
		Properties prop = new Properties();
		Reader reader = null;
		createCycleRate(percentWordLength);
		try {
			reader = new FileReader(CONFIG_FILE);
			prop.load(reader);
			loadData(prop);
			numLoop = Integer.parseInt(prop.getProperty("numLoop"));
			populationSize = Integer.parseInt(prop
					.getProperty("populationSize"));
			crossOverRate = Double.parseDouble(prop
					.getProperty("crossOverRate"));
			mutationRate = Double.parseDouble(prop.getProperty("mutationRate"));
			thresdhold = Double.parseDouble(prop.getProperty("thresdhold"));
		} catch (IOException e) {
			logger.error("error when load config file", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("error when load config file", e);
				}
			}
		}
	}

	/**
	 * 
	 */
	private void loadData(Properties prop) {
		Connector connector = null;
		try {
			connector = new Connector(prop);
			connector.connect();
			String queryLoad = prop.getProperty("queryLoad");
			ResultSet rs = connector.executeQuery(queryLoad);
			while (rs.next()) {
				String word = rs.getString("word");
				vocab.put(word, rs.getDouble("df"));
			}
		} catch (Exception e) {
			logger.error("Error when loading data", e);
		} finally {
			connector.close();
		}
	}

	public byte[] segment(String line) {
		List<String> syllables = Arrays.asList(line.split("\\s+"));
		List<byte[]> segms = generateInit(syllables.size());
		double[] cycle = new double[segms.size()];

		for (int i = 0; i < numLoop; ++i) {
			// compute fitness function
			for (int j = 0; j < segms.size(); ++j) {
				cycle[j] = fitness(syllables, segms.get(j));
			}
			createCycleRate(cycle);
			// create new generation
			List<byte[]> newSegms = new ArrayList<byte[]>();
			for (int j = 0; j < populationSize; ++j) {
				int mother = getRandom(cycle, false);
				int father = getRandom(cycle, false);
				byte[] child = null;
				if (Math.random() < crossOverRate) {
					child = crossOver(segms.get(mother), segms.get(father));
				}
				if (child == null) {
					child = segms.get(mother).clone();
				}
				if (Math.random() < mutationRate) {
					child = mutation(child);
				}

				if (fitness(syllables, child) / getNumSegm(child) >= thresdhold) {
					return child;
				}
				newSegms.add(child);
			}
			segms = newSegms;
		}
		int index = 0;
		double maxScore = 0;
		double temp = 0;
		for (int i = 0; i < segms.size(); ++i) {
			temp = fitness(syllables, segms.get(i));
			if (maxScore < temp) {
				maxScore = temp;
				index = i;
			}
		}
		return segms.get(index);
	}

	/**
	 * @param child
	 * @return
	 */
	private byte[] mutation(byte[] child) {
		int i = (int) (Math.random() * (child.length - 1));
		byte tmp = child[i];
		child[i] = child[i + 1];
		child[i + 1] = tmp;
		return child;
	}

	/**
	 * @param bs
	 * @param bs2
	 * @return
	 */
	private byte[] crossOver(byte[] mother, byte[] father) {
		mother = mother.clone();
		int index = (int) (Math.random() * mother.length);
		for (int i = index; i < mother.length; ++i) {
			mother[i] = father[i];
		}
		return mother;
	}

	public List<byte[]> generateInit(int lineLength) {
		List<byte[]> result = new ArrayList<byte[]>();
		for (int i = 0; i < populationSize; ++i) {
			result.add(randomSegm(lineLength));
		}
		return result;
	}

	public byte[] randomSegm(int lineLength) {
		byte[] segms = new byte[lineLength];
		int start = 0;
		int end = 0;
		byte state = 0;
		while (start < lineLength) {
			end = start + getRandom(percentWordLength, false) + 1;
			end = Math.min(end, lineLength);
			for (int i = start; i < end; ++i) {
				segms[i] = state;
			}
			start = end;
			state = (byte) ((state + 1) % 2);
		}
		return segms;
	}

	public double fitness(List<String> syllables, byte[] segm) {
		byte temp = segm[0];
		double score = 0;
		int start = 0;
		for (int i = 1; i < segm.length; ++i) {
			if (temp != segm[i]) {
				score += mutualInfo(syllables.subList(start, i));
				start = i;
				temp = segm[i];
			}
			if (i == segm.length - 1) {
				score += mutualInfo(syllables.subList(start, segm.length));
			}
		}
		return score;
	}

	public double mutualInfo(List<String> word) {
		if (word.size() == 1) {
			Double score = vocab.get(word.get(0));
			return score == null ? 0 : score;
		}
		int subLength = word.size() / 2;
		subLength = word.size() % 2 == 0 ? subLength : subLength + 1;
		StringBuilder cw = new StringBuilder();
		StringBuilder lw = new StringBuilder();
		StringBuilder rw = new StringBuilder();
		String syllable = null;
		for (int i = 0, n = word.size(); i < n; ++i) {
			syllable = word.get(i);
			cw.append(syllable).append(' ');
			if (i < subLength) {
				lw.append(syllable).append(' ');
			}
			if (i >= n - subLength) {
				rw.append(syllable).append(' ');
			}
		}
		if (cw.length() > 0) {
			cw.deleteCharAt(cw.length() - 1);
		}
		if (lw.length() > 0) {
			lw.deleteCharAt(lw.length() - 1);
		}
		if (rw.length() > 0) {
			rw.deleteCharAt(rw.length() - 1);
		}
		Double pcw = vocab.get(cw.toString());
		if (pcw == null) {
			return 0;
		}
		Double prw = vocab.get(rw.toString());
		if (pcw == null) {
			pcw = 0d;
		}
		Double plw = vocab.get(lw.toString());
		double temp = prw + plw - pcw;
		return temp == 0 ? Double.MAX_VALUE : pcw / temp;
	}

	public int getRandom(double[] cycle, boolean createNew) {
		if (createNew) {
			createCycleRate(cycle);
		}
		double random = Math.random();
		for (int i = 0; i < cycle.length; ++i) {
			if (cycle[i] > random) {
				return i;
			}
		}
		return -1;
	}

	public int getRandom(double[] cycle) {
		return getRandom(cycle, false);
	}

	public void createCycleRate(double[] cycle) {
		double total = 0;
		for (int i = 0, n = cycle.length; i < n; ++i) {
			total += cycle[i];
		}
		double previous = 0;
		for (int i = 0, n = cycle.length; i < n; ++i) {
			cycle[i] = cycle[i] / total + previous;
			previous = cycle[i];
		}
	}

	public static int getNumSegm(byte[] segm) {
		byte state = segm[0];
		int counter = 1;
		for (int i = 1; i < segm.length; ++i) {
			if (segm[i] != state) {
				++counter;
				state = segm[i];
			}
		}
		return counter;
	}
}
