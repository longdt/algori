package com.solt.algorithm.dom;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.solt.algorithm.util.NodeUtils;


public class TreeMatchingAlgorithm {
	public List<Template> mDR(Node tree, int maxNumTagNode,
			float similarThreshold) {
		if (NodeUtils.getTreeDepth(tree) >= 3) {
			combComp(tree.getChildNodes(), maxNumTagNode);
		}
		return null;
	}
	private void combComp(NodeList childNodes, int maxNumTagNode) {
		List<String> tagStringSt = null;
		List<String> tagStringK = null;
		for (int i = 0; i < maxNumTagNode; ++i) {
			for (int j = i; j < maxNumTagNode; ++j) {
				if (childNodes.item(i + 2 * j - 1) != null) {
					int st = i;
					for (int k = i + j; k < childNodes.getLength(); k += j) {
						if (childNodes.item(k + j - 1) != null) {
							// get TagString of childeNodes from St to (k - 1).
							tagStringSt = new ArrayList<String>();
							for (int l = st; l < k; ++l) {
								tagStringSt.addAll(NodeUtils
										.getFullStringNode(childNodes.item(l)));
							}
							// get TagString of childeNodes from k to (k + j -
							// 1).
							tagStringK = new ArrayList<String>();
							for (int l = k, n = k + j; l < n; ++l) {
								tagStringSt.addAll(NodeUtils
										.getFullStringNode(childNodes.item(l)));
							}
							STMComparator.editDist(tagStringSt, tagStringK);
							st = k;
						}
					}
				}
			}
		}
	}
	
	public static void net(Node root, float threshold) {
		traverseAndMatch(root, threshold);
	}

	private static void traverseAndMatch(Node node, float threshold) {
		if (NodeUtils.getTreeDepth(node) >= 3) {
			// traversing tree in post-order.
			NodeList nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); ++i) {
				traverseAndMatch(nodeList.item(i), threshold);
			}

			// matching tree.
			match(node, threshold);
		}
	}

	private static void match(Node node, float threshold) {
		NodeList nodeList = node.getChildNodes();
		List<Node> children = new ArrayList<Node>();
		for (int i = 0; i < nodeList.getLength(); ++i) {
			children.add(nodeList.item(i));
		}
		Node childFirst = null;
		boolean aligned = false;
		boolean hasLink = false;
		boolean continuous = true;
		while (!children.isEmpty()) {
			childFirst = children.remove(0);
			hasLink = false;
			for (int i = 0; i < children.size(); ++i) {
				if (STMComparator.treeMatching(childFirst, children.get(i)) > threshold) {
					aligned = alignAndLink();
					if (aligned) {
						children.remove(i);
						hasLink = true;
					}
				}
			}
			if (hasLink) {
				genNodePattern(childFirst);
			} else {
				continuous = false;
			}
		}
		if (continuous) {
			genNodePattern(node);
		}
	}

	private static boolean alignAndLink() {
		return true;
	}

	private static void genNodePattern(Node childFirst) {

	}

}
