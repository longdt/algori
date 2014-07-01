/**
 * 
 */
package com.solt.algorithm;

import java.io.IOException;
import java.util.List;

/**
 * @author thienlong
 *
 */
public interface SynonymsEngine {
	/** get a list synonym of a given string.<br>Note:  <b>Never return empty list</b>
	 * @param text
	 * @return a list synonym of a given string otherwise null.
	 * @throws IOException
	 */
	List<String> getSynonym(String text) throws IOException;
}
