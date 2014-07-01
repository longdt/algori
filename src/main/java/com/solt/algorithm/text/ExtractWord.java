package com.solt.algorithm.text;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import com.solt.sql.Connector;


/**
 * This module perform convert documents to vectors.
 * 
 * @author iCream
 * 
 */
public class ExtractWord {
	private static final String FILE_NAME = "database.properties";
	private Connector connector;
	private Map<String, WordCounter> bagWord;
	private int numDocs;
	private int includeMin = 10;
	private int includeMax = Integer.MAX_VALUE;

	public ExtractWord() throws IOException, ClassNotFoundException,
			SQLException, InstantiationException, IllegalAccessException {
		Properties pros = new Properties();
		InputStream in = new FileInputStream(FILE_NAME);
		pros.load(in);
		connector = new Connector(pros);
		connector.connect();
		bagWord = new HashMap<String, WordCounter>();
		try {
			includeMin = Integer.parseInt(pros.getProperty("includeMin"));
			includeMax = Integer.parseInt(pros.getProperty("includeMax"));
		} catch (NumberFormatException e) {

		}
	}

	/**
	 * perform the task.
	 * 
	 * @throws SQLException
	 */
	public void execute() throws SQLException {
		String query = "SELECT COUNT(id) FROM Topic";
		ResultSet rs = connector.executeQuery(query);
		if (rs.next()) {
			numDocs = rs.getInt(1);
			rs.close();
			query = "SELECT content FROM Topic";
			rs = connector.executeQuery(query);
			String content = null;
			String newContent = null;
			while (rs.next()) {
				content = rs.getString("content");
				newContent = refineDoc(content);
				extractWord(newContent);
			}
		}
	}

	/**
	 * Extracting word from a given content.
	 * 
	 * @param content
	 */
	public void extractWord(String content) {
		Set<String> wordsContent = new HashSet<String>();
		Scanner in = new Scanner(content);
		String word = null;
		WordCounter counter = null;
		while (in.hasNext()) {
			word = refineWord(in.next());
			if (word == null) {
				continue;
			}
			if (wordsContent.add(word)) {
				// if this word not in wordContent yet.
				if (bagWord.containsKey(word)) {
					counter = bagWord.get(word);
					counter.increaseCountWords();
					counter.increaseNumDocs();
				} else {
					bagWord.put(word, new WordCounter());
				}
			} else {
				// if this word already in wordContent.
				counter = bagWord.get(word);
				counter.increaseCountWords();
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
		if (word.length() < 2 || word.length() > 7) {
			return null;
		}

		char c = 0;
		StringBuilder refineWord = new StringBuilder();
		for (int i = 0, n = word.length(); i < n; ++i) {
			c = Character.toUpperCase(word.charAt(i));
			if ((c > 'A' && c < 'Y') || (c >= '\u00C0' && c <= '\u1EF9')) {
				refineWord.append(c);
			} else {
				return null;
			}
		}
		return refineWord.toString();
	}

	/**
	 * refine a given document by using regular expression.
	 * 
	 * @param content
	 * @param regex
	 * @return refined document.
	 */
	public static String refineDoc(String content) {
		return content.replaceAll("&lt;/*((span)|(div)|(br)|(p)|(td)|(tr)"
				+ "|(h1)|(h2)|(h3)|(em)|(a)|(b)|(i)|(img)|(style)).*?&gt;", "");
	}

	/**
	 * close database connection.
	 */
	public void finish() {
		connector.close();
	}

	/**
	 * Build a Vocabulary.
	 * 
	 * @throws SQLException
	 */
	public void buildVocabulary() throws SQLException {
		createTable(connector);
		String query = "INSERT INTO Vocabulary(word, idf) VALUE(?, ?)";
		PreparedStatement preSta = connector.executeSQL(query);
		WordCounter counter = null;
		int countWords = 0;
		int numDocs = 0;
		float idf = 0;

		for (Entry<String, WordCounter> entry : bagWord.entrySet()) {
			counter = entry.getValue();
			countWords = counter.getCountWords();
			if (countWords < includeMax && countWords > includeMin) {
				numDocs = counter.getNumDocs();
				idf = (float) (Math.log(this.numDocs / (float) numDocs));
				preSta.setString(1, entry.getKey());
				preSta.setDouble(2, idf);
				preSta.executeUpdate();
			}
		}
	}

	/**
	 * create table if it is'nt exist.
	 * 
	 * @param connector
	 * @throws SQLException
	 */
	public void createTable(Connector connector) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS Vocabulary"
				+ "(vocabid INT AUTO_INCREMENT NOT NULL,"
				+ "word VARCHAR(20), idf FLOAT, PRIMARY KEY(vocabid)) "
				+ "ENGINE=MyISAM DEFAULT CHARSET=utf8 "
				+ "COLLATE=utf8_unicode_ci;";
		connector.executeUpdate(sql);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ExtractWord extractor = new ExtractWord();
		extractor.execute();
		extractor.buildVocabulary();
		extractor.finish();
	}

}
