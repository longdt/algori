package com.solt.algorithm.text;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;

import com.solt.sql.Connector;



/**
 * This module perform convert documents to vectors.
 * 
 * @author ThienLong
 * 
 */
public class VectorNormalizor {
	private static final String FILE_NAME = "database.properties";
	private Connector connector;
	private Map<String, Float> bagWord;

	public VectorNormalizor() throws ClassNotFoundException, SQLException,
			IOException, InstantiationException, IllegalAccessException {
		Properties pros = new Properties();
		InputStream in = new FileInputStream(FILE_NAME);
		pros.load(in);
		connector = new Connector(pros);
		connector.connect();
		bagWord = new LinkedHashMap<String, Float>();
	}

	/**
	 * load a vocabulary in database.
	 * 
	 * @param connector
	 * @param bagWord
	 * @throws SQLException
	 */
	public static void loadVocab(Connector connector, Map<String, Float> bagWord)
			throws SQLException {
		String query = "SELECT word, idf FROM Vocabulary";
		ResultSet rs = connector.executeQuery(query);
		while (rs.next()) {
			bagWord.put(rs.getString("word"), rs.getFloat("idf"));
		}
	}

	/**
	 * normalize all document in database to vector form.
	 * 
	 * @throws SQLException
	 */
	public void normalize() throws SQLException {
		String query = "SELECT id, content, vectorweight, size FROM Topic";
		PreparedStatement preSta = connector.executeSQL(query,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = preSta.executeQuery();
		String content = null;
		StringBuilder vectorString = null;
		Map<String, Float> vectorWeight = null;
		float size = 0;
		while (rs.next()) {
			content = rs.getString("content");
			vectorWeight = new HashMap<String, Float>();
			size = normalizeVector(content, bagWord, vectorWeight);

			vectorString = new StringBuilder();
			for (Entry<String, Float> entry : vectorWeight.entrySet()) {
				vectorString.append(" " + entry.getKey() + " "
						+ entry.getValue());
			}
			rs.updateString("vectorweight", vectorString.substring(1));
			rs.updateFloat("size", size);
			rs.updateRow();
		}
	}

	/**
	 * normalize a given content to boolean vector and weight vector.
	 * 
	 * @param content
	 * @param bagWord
	 * @param vector
	 * @param vectorWeight
	 * @return the size of weight vector.
	 */
	public static float normalizeVector(String content,
			Map<String, Float> bagWord, Map<String, Float> vectorWeight) {
		Map<String, Integer> wordsDoc = new HashMap<String, Integer>();
		int tfMax = parseWord(content, wordsDoc);
		float size = 0f;
		for (Entry<String, Integer> entry : wordsDoc.entrySet()) {
			String word = entry.getKey();
			if (bagWord.containsKey(word)) {
				float weight = entry.getValue() * bagWord.get(word) / tfMax;
				size += weight * weight;
				vectorWeight.put(word, weight);
			}
		}
		return (float) Math.sqrt(size);
	}

	public static Map<String, Float> parseVector(String vectorString) {
		Map<String, Float> vector = new HashMap<String, Float>();
		Scanner in = new Scanner(vectorString);
		while (in.hasNext()) {
			vector.put(in.next(), in.nextFloat());
		}
		return vector;
	}

	public static String vectorToString(Map<String, Float> vectorWeight) {
		StringBuilder vectorString = new StringBuilder();
		for (Entry<String, Float> entry : vectorWeight.entrySet()) {
			vectorString.append(" " + entry.getKey() + " " + entry.getValue());
		}
		return (vectorString.length() > 0) ? vectorString.substring(1) : "";

	}

	/**
	 * parse word from a given content then associate with a given map.
	 * 
	 * @param content
	 * @param wordDocs
	 * @return itfMax of given content.
	 */
	public static int parseWord(String content, Map<String, Integer> wordDocs) {
		String refinedContent = ExtractWord.refineDoc(content);
		Scanner in = new Scanner(refinedContent);
		String word = null;
		int itfMax = 0;
		int tf = 0;
		while (in.hasNext()) {
			word = ExtractWord.refineWord(in.next());
			if (word == null) {
				continue;
			}

			// if this word not in wordContent yet.
			if (wordDocs.containsKey(word)) {
				tf = wordDocs.get(word) + 1;
				wordDocs.put(word, tf);
			} else {
				wordDocs.put(word, 1);
			}
		}

		for (Integer counter : wordDocs.values()) {
			if (counter > itfMax) {
				itfMax = counter;
			}
		}
		return itfMax;
	}

	/**
	 * close database connection.
	 */
	public void finish() {
		connector.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		long timer = System.currentTimeMillis();
		VectorNormalizor normalizor = new VectorNormalizor();
		VectorNormalizor.loadVocab(normalizor.connector, normalizor.bagWord);
		normalizor.normalize();
		normalizor.finish();
		timer = System.currentTimeMillis() - timer;
		System.out.println(timer);
	}

}
