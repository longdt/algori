/**
 * 
 */
package vn.vccorp.algorithm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.solt.algorithm.StringCompareUtils;

/**
 * @author thienlong
 *
 */
public class StringCompareUtilsTest {

	/**
	 * Test method for {@link com.solt.algorithm.StringCompareUtils#generateNGram(java.lang.String, int)}.
	 */
	@Test
	public void testGenerateNGram() {
		String content = "xin chao, cac ban. Rat vui dc lam quen";
		List<String> ngram = StringCompareUtils.generateNGram(content, 2);
		for (String token : ngram) {
			System.out.println(token);
		}
	}
	
	@Test
	public void testEditDistNormalized() {
		String content1 = "hello word";
		String content2 = "hllo world";
		System.out.println(StringCompareUtils.editDistance(content1, content2));
	}

}
