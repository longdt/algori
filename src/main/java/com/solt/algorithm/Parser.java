/**
 * 
 */
package com.solt.algorithm;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

import com.solt.algorithm.StringCompareUtils;
import com.solt.algorithm.util.VietNameseCharacter;

/**
 * @author thienlong
 * 
 */
public class Parser {
	private static final int MAX_LENGTH = 200;
	private String delimiter;
	private Analyzer analyzer;
	/**
	 * 
	 */
	public Parser(String delimiter, int ngramSize) {
		this(new ShingleAnalyzerWrapper(Version.LUCENE_CURRENT, ngramSize), delimiter);
	}
	
	public Parser(Analyzer analyzer, String delimiter) {
		this.analyzer = analyzer;
		this.delimiter = delimiter;
	}

	/**
	 * Extracting word from a given content.
	 * 
	 * @param content
	 */
	public void extractWord(String content, Map<String, TermCounter> bagWord) {
		if (delimiter == null) {
			updateVocab(bagWord, generateNGram(content));
			return;
		}
		Scanner tokenizer = new Scanner(content);
		tokenizer.useDelimiter(delimiter);
		String line = null;
		List<String> ngram = null;
		while (tokenizer.hasNext()) {
			line = tokenizer.next().trim();
			if (line.isEmpty()) {
				continue;
			}
			ngram = generateNGram(line);
			updateVocab(bagWord, ngram);
		}
		tokenizer.close();
	}

	/**
	 * @param line
	 * @param ngramSize
	 * @return
	 */
	public List<String> generateNGram(String line) {
		TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(line));
		TermAttribute termAttr = tokenStream.addAttribute(TermAttribute.class);
		List<String> ngrams = new ArrayList<String>();
		try {
			while (tokenStream.incrementToken()) {
				ngrams.add(termAttr.term());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return ngrams;
	}

	private void updateVocab(Map<String, TermCounter> bagWord,
			List<String> words) {
		TermCounter counter = null;
		Set<String> wordsChecker = new HashSet<String>();
		for (String word : words) {
			word = refineWord(word);
			if (word == null || word.isEmpty()) {
				continue;
			}
			if (wordsChecker.add(word)) {
				counter = bagWord.get(word);
				if (counter == null) {
					// if this word not in bagWord yet.
					counter = new TermCounter();
					if (VietNameseCharacter.isSpecialWord(word)) {
						counter
								.setTerm(VietNameseCharacter
										.getNormalWord(word));
					}
					counter.setTermFreq(1);
					counter.setDocFreq(1);
					bagWord.put(word, counter);
				} else {
					// if this word already in wordContent.
					counter.increaseTermFreq();
					counter.increaseDocFreq();
				}
			} else {
				// if this word already in wordContent.
				counter = bagWord.get(word);
				counter.increaseTermFreq();
			}
		}
	}

	/**
	 * refine a word. Checking whether it is correct.
	 * 
	 * @param word
	 * @return refined word.
	 */
	public static String refineWord(String word) {
		if (word.length() > MAX_LENGTH) {
			return null;
		}
		char c = 0;
		StringBuilder refineWord = new StringBuilder();
		int lastIndex = 0;
		for (int i = 0, n = word.length(); i < n; ++i) {
			c = Character.toLowerCase(word.charAt(i));
			if ((c == ' ') || (c >= 'a' && c <= 'y') || (c >= '0' && c <= '9')
					|| VietNameseCharacter.isSpecChar(c)) {
				refineWord.append(c);
			} else if (VietNameseCharacter.isSpecSig(c)) {
				lastIndex = refineWord.length() - 1;
				refineWord.setCharAt(lastIndex, VietNameseCharacter
						.refineSpecChar(refineWord.charAt(lastIndex), c));
			}
		}
		return refineWord.toString().trim();
	}

	/**
	 * refine a given document by using regular expression.
	 * 
	 * @param content
	 * @param regex
	 * @return refined document.
	 */
	public static String refineDoc(String content, String regex,
			String replacement) {
		return regex != null ? content.replaceAll(regex, " ") : content;
	}
}
