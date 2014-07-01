package com.solt.algorithm.dom;

import java.util.Comparator;

import org.w3c.dom.Node;

import com.solt.algorithm.util.NodeUtils;

public class NumNodeComparator implements Comparator<Node> {

	@Override
	public int compare(Node o1, Node o2) {
		return NodeUtils.getNumNodes(o1) - NodeUtils.getNumNodes(o2);
	}

} 
