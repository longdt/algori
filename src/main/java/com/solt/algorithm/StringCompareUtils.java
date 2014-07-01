package com.solt.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 
 */

/**
 * @author thienlong
 * 
 */
public class StringCompareUtils {

	public static int editDistance(String stringSrc, String stringDes) {
		int[][] matching = new int[stringSrc.length() + 1][stringDes.length() + 1];
		for (int i = 1; i <= stringSrc.length(); ++i) {
			matching[i][0] = i;
		}
		for (int i = 1; i <= stringDes.length(); ++i) {
			matching[0][i] = i;
		}
		char charSrc = 0;
		char charDes = 0;
		for (int i = 1; i <= stringSrc.length(); ++i) {
			for (int j = 1; j <= stringDes.length(); ++j) {
				charSrc = stringSrc.charAt(i - 1);
				charDes = stringDes.charAt(j - 1);
				int p = (charSrc == charDes) ? 0 : 1;
				matching[i][j] = Math.min(matching[i - 1][j - 1] + p,
						matching[i - 1][j] + 1);
				matching[i][j] = Math.min(matching[i][j],
						matching[i][j - 1] + 1);
			}
		}
		return matching[stringSrc.length()][stringDes.length()];
	}

	/**
	 * get normalize edit distance string of 2 given string.
	 * 
	 * @param stringSrc
	 * @param stringDes
	 * @return score which was normalized
	 */
	public static float editDistNormalized(String stringSrc, String stringDes) {
		float score = editDistance(stringSrc, stringDes);
		float percent = (score * 2) / (stringSrc.length() + stringDes.length());
		return percent;
	}

	/**
	 * generate ngram from a given <b>content</b>.
	 * 
	 * @param line
	 * @param ngram
	 * @return a list ngram
	 */
	public static List<String> generateNGram(String line, int ngram) {
		StringTokenizer tokenizer = new StringTokenizer(line);
		List<String> tokens = new ArrayList<String>();
		List<String> previousN_1Gram = new ArrayList<String>();
		String nextToken = null;
		StringBuilder builder = new StringBuilder();
		while (tokenizer.hasMoreTokens()) {
			nextToken = tokenizer.nextToken();
			if (previousN_1Gram.size() == ngram) {
				previousN_1Gram.remove(0);
			}

			previousN_1Gram.add(nextToken);
			builder.setLength(0);
			for (String token : previousN_1Gram) {
				builder.append(token);
				tokens.add(builder.toString());
				builder.append(' ');
			}
		}
		return tokens;
	}

	/**
	 * similarity with
	 * {@link StringCompareUtils#generateNGram(String line, int ngram)} but a
	 * ngram list was store in a given <b>tokens</b>
	 * 
	 * @param line
	 * @param ngram
	 * @param tokens
	 */
	public static void generateNGram(String line, int ngram, List<String> tokens) {
		tokens.addAll(generateNGram(line, ngram));
	}

}
