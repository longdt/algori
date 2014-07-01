/**
 * 
 */
package vn.vccorp.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

import com.solt.algorithm.Parser;

/**
 * @author thienlong
 *
 */
public class ParserTest {

	/**
	 * Test method for {@link com.solt.algorithm.Parser#generateNGram(java.lang.String)}.
	 */
	@Test
	public void testGenerateNGram() {
		Parser parser = new Parser(null, 4);
		String text = "điện thoại di đông thời trang sanh điệu";
		System.out.println(parser.generateNGram(text));
	}

}
