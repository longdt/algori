/**
 * 
 */
package com.solt.algorithm;

/**
 * @author thienlong
 * 
 */
public class TermCounter {
	private String term;
	private int termFreq;
	private int docFreq;
	
	public TermCounter() {
		
	}
	
	public TermCounter(String term, int termFreq, int docFreq) {
		this.term = term;
		this.termFreq = termFreq;
		this.docFreq = docFreq;
	}
	
	public void increaseTermFreq() {
		++termFreq;
	}
	
	public void increaseTermFreq(int freq) {
		termFreq += freq;
	}
	
	public void increaseDocFreq() {
		++docFreq;
	}
	
	public void increaseDocFreq(int freq) {
		docFreq += freq;
	}
	
	/**
	 * @return the termFreq
	 */
	public int getTermFreq() {
		return termFreq;
	}
	/**
	 * @param termFreq the termFreq to set
	 */
	public void setTermFreq(int termFreq) {
		this.termFreq = termFreq;
	}
	/**
	 * @return the docFreq
	 */
	public int getDocFreq() {
		return docFreq;
	}
	/**
	 * @param docFreq the docFreq to set
	 */
	public void setDocFreq(int docFreq) {
		this.docFreq = docFreq;
	}

	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(String term) {
		this.term = term;
	}
}
