package com.solt.algorithm.text;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;

import com.solt.sql.Connector;


public class SimilarityComparator {
	private static final String FILE_NAME = "database.properties";
	private Connector connector;
	private Map<String, Float> bagWord;
	private static Logger logger = Logger.getLogger(SimilarityComparator.class);
	private Set<VectorDoc> vectorDocs;
	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public SimilarityComparator() throws ClassNotFoundException, SQLException,
			IOException, InstantiationException, IllegalAccessException {
		Properties pros = new Properties();
		InputStream in = new FileInputStream(FILE_NAME);
		pros.load(in);
		connector = new Connector(pros);
		connector.connect();
		bagWord = new LinkedHashMap<String, Float>();
		VectorNormalizor.loadVocab(connector, bagWord);
		vectorDocs = getVectorDocs();
	}

	public static Map<String, Float> parseVector(String vectorString) {
		Map<String, Float> vector = new HashMap<String, Float>();
		Scanner in = new Scanner(vectorString);
		in.useDelimiter(" |=");
		while (in.hasNext()) {
			vector.put(in.next(), in.nextFloat());
		}
		return vector;
	}

	public float similar(String bodyContent) throws Exception {
		Map<String, Float> vectorWeightSrc = new HashMap<String, Float>();
		float sizeSrc = VectorNormalizor.normalizeVector(bodyContent, bagWord, vectorWeightSrc);

		Map<String, Float> vectorWeight = null;
		float size = 0f;
		float maxPercent = 0;
		float cosAngel = 0;
		for (VectorDoc element : vectorDocs) {
				vectorWeight = element.getVectorWeight();
				size = element.getSize();
				cosAngel = cosine(vectorWeightSrc, sizeSrc, vectorWeight,
						size);
				if (cosAngel > maxPercent) {
					maxPercent = cosAngel;
				}
		}
		return maxPercent;
	}

	public static float cosine(Map<String, Float> vectorSrc, float sizeSrc,
			Map<String, Float> vector, float size) {
		float multiVector = 0;
		String word = null;
		Float tmpW = null;
		for (Entry<String, Float> entry : vectorSrc.entrySet()) {
			word = entry.getKey();
			if ((tmpW = vector.get(word)) != null) {
				multiVector += tmpW * entry.getValue();
			}
		}
		return (float) (multiVector / (sizeSrc * size));
	}

	public void finish() {
		connector.close();
	}

	public static void main(String[] args) throws Exception {
		SimilarityComparator comparator = new SimilarityComparator();
		long timer = System.currentTimeMillis();
		comparator.similar("adasd");
		timer = System.currentTimeMillis() - timer;
		System.out.println(timer);
		comparator.finish();
	}

	public Set<VectorDoc> getVectorDocs() {
		Set<VectorDoc> vectorDocs = new HashSet<VectorDoc>();
		String query = "SELECT id, vectorweight, size FROM Topic";
		VectorDoc vector = null;
		ResultSet rs = null;
		try {
			rs = connector.executeQuery(query);
			while (rs.next()) {
				// fill up information into vector.
				vector = new VectorDoc();
				vector.setDocid(rs.getLong("id"));
				vector.setSize(rs.getFloat("size"));
				vector.setVectorWeight(VectorNormalizor.parseVector(rs
						.getString("vectorweight")));
				vectorDocs.add(vector);
			}
		} catch (Exception e) {
			logger.error("The error occur when get vectordocs", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("The Error occur when closing resultset object", e);
				}
			}
		}
		return vectorDocs;
	}


}
