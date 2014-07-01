package com.solt.algorithm.text;

public class WordCounter {
	public void increaseCountWords() {
		++countWords;
	}
	
	public void increaseNumDocs() {
		++numDocs;
	}
	
	public int getCountWords() {
		return countWords;
	}
	public void setCountWords(int countWords) {
		this.countWords = countWords;
	}
	public int getNumDocs() {
		return numDocs;
	}
	public void setNumDocs(int numDocs) {
		this.numDocs = numDocs;
	}
	private int countWords = 1;
	private int numDocs = 1;
}
