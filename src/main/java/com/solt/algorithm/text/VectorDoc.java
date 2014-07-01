package com.solt.algorithm.text;

import java.util.Map;

public class VectorDoc {
	private Map<String, Float> vectorWeight;
	private float size;
	private long docid;
	
	public VectorDoc() {
		
	}
	
	public VectorDoc(long docid, Map<String, Float> vectorWeight, float size) {
		this.docid = docid;
		this.size = size;
		this.vectorWeight = vectorWeight;
	}

	public Map<String, Float> getVectorWeight() {
		return vectorWeight;
	}
	public void setVectorWeight(Map<String, Float> vectorWeight) {
		this.vectorWeight = vectorWeight;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public long getDocid() {
		return docid;
	}
	public void setDocid(long docid) {
		this.docid = docid;
	}
}
