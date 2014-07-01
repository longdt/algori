package com.solt.algorithm.dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Node;

import com.solt.algorithm.util.NodeUtils;


public class STMComparator {

	/**
	 * find data region of a given tree.
	 * 
	 * @param tree
	 * @param k
	 * @param threshold
	 * @return all data region associate with given node.
	 */
	public static Map<Node, List<int[]>> findDRs(Node tree, int k,
			float threshold) {
		if (NodeUtils.getTreeDepth(tree) >= 3) {
			Map<Node, List<int[]>> nodeDRs = new HashMap<Node, List<int[]>>();
			List<int[]> drsPerNode = identDRs(1, tree, k, threshold);
			if (drsPerNode != null) {
				nodeDRs.put(tree, drsPerNode);
			}
			Map<Node, List<int[]>> tempDRs = new HashMap<Node, List<int[]>>();
			Map<Node, List<int[]>> childDRs = null;
			Map<Node, List<int[]>> uncoveredDRs = null;
			List<Node> subTrees = NodeUtils.getChildrenNode(tree);
			Node subTree = null;
			for (int i = 0; i < subTrees.size(); ++i) {
				subTree = subTrees.get(i);
				childDRs = findDRs(subTree, k, threshold);
				uncoveredDRs = uncoveredDRs(nodeDRs, childDRs, subTree);
				if (uncoveredDRs != null) {
					tempDRs.putAll(uncoveredDRs);
				}

			}
			nodeDRs.putAll(tempDRs);
			return nodeDRs.isEmpty() ? null : nodeDRs;
		}
		return null;
	}
	
	public static Map<Node, List<int[]>> filterDRs(Map<Node, List<int[]>> nodeDRs) {
		if (nodeDRs == null) {
			return null;
		}
		int counter = 0;
		int drCounter = 0;
		for (List<int[]> drs : nodeDRs.values()) {
			for (int[] dr : drs) {
				counter += dr[3];
				++drCounter;
			}
		}
		Iterator<Entry<Node, List<int[]>>> iterator = nodeDRs.entrySet().iterator();
		List<int[]> drs = null;
		Iterator<int[]> drIter = null;
		int[] dr = null;
		float threshold = counter / (float) drCounter;
		while (iterator.hasNext()) {
			Entry<Node, List<int[]>> entry =iterator.next();
			drs = entry.getValue();
			drIter = drs.iterator();
			while (drIter.hasNext()) {
				dr = drIter.next();
				if (dr[3] < threshold) {
					drIter.remove();
				}
			}
			if (drs.isEmpty()) {
				iterator.remove();
			}
		}
		return nodeDRs;
	}

	/**
	 * identify DRs that comprise from children of given tree.
	 * 
	 * @param start
	 * @param tree
	 * @param k
	 * @param threshold
	 * @return 
	 */
	public static List<int[]> identDRs(int start, Node tree, int k,
			float threshold) {
		int[] maxDR = new int[4];
		List<Node> childNodes = NodeUtils.getChildrenNode(tree);
		if (childNodes == null) {
			return null;
		}
		int size = childNodes.size();
		int maxComb = Math.min(size / 2, k);
		for (int i = 1; i <= maxComb; ++i) { // compute for each i-combination
			for (int f = start; f < start + i; ++f) { // compute start windows
				// slide
				boolean flag = true;
				int[] curDR = new int[4];
				for (int j = f; j < size; j += i) {
					if (distanceTree(childNodes, i, j) >= threshold) {
						if (flag) {
							curDR[0] = i;
							curDR[1] = j;
							curDR[2] = 2 * i;
							curDR[3] = NodeUtils.getNumNodes(childNodes, j - 1, j - 1 + curDR[2]);
							flag = false;
						} else {
							curDR[3] += NodeUtils.getNumNodes(childNodes, j - 1 + i, j - 1 + 2 * i);
							curDR[2] += i;
						}
					} else if (!flag) {
						break;
					}
				}
				if ((maxDR[2] <= curDR[2])
						&& (maxDR[1] == 0 || (curDR[1] <= maxDR[1]))) {
					maxDR = curDR;
				}
			}
		}
		if (maxDR[2] != 0) {
			List<int[]> maxDRs = new ArrayList<int[]>();
			maxDRs.add(maxDR);
			if (maxDR[1] + maxDR[2] - 1 != size) {
				List<int[]> otherDR = identDRs(maxDR[1] + maxDR[2], tree, k,
						threshold);
				if (otherDR != null) {
					maxDRs.addAll(otherDR);
				}
			}
			return maxDRs;
		}
		return null;
	}

	/**
	 * get Data Region in childDRs that are'nt covered by any region in nodeDRs.
	 * @param nodeDRs
	 * @param childDRs
	 * @return
	 */
	public static Map<Node, List<int[]>> uncoveredDRs(
			Map<Node, List<int[]>> nodeDRs, Map<Node, List<int[]>> childDRs,
			Node childNode) {
		if (childDRs == null) {
			return null;
		}

		for (Entry<Node, List<int[]>> entry : nodeDRs.entrySet()) {
			Node node = entry.getKey();
			List<int[]> drs = entry.getValue();
			for (int[] dr : drs) {
				if (cover(node, childNode, dr)) {
					return null;
				}
			}
		}
		return childDRs;
	}

	/**
	 * check whether a given <i>parentNode</i> with data region <i>dr</i> cover a given <i>childNode</i>.
	 * @param parentNode
	 * @param childNode
	 * @param dr
	 * @return true if cover, false otherwise.
	 */
	public static boolean cover(Node parentNode, Node childNode, int[] dr) {
		List<Node> childNodes = NodeUtils.getChildrenNode(parentNode);
		Node subNode = null;
		for (int i = dr[1] - 1, n = dr[2] + dr[1] - 1; i < n; ++i) {
			subNode = childNodes.get(i);
			if ((subNode.compareDocumentPosition(childNode) & Node.DOCUMENT_POSITION_CONTAINED_BY) != 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * compute distance between jth child with (j+1)th child of given node.
	 * 
	 * @param tree
	 * @param i
	 * @param j
	 * @return
	 */
	@Deprecated
	public static float distanceString(List<Node> childNodes, int i, int j) {
		--j; // convert jth subtree to indexed subtree

		Node node1 = null;
		Node node2 = null;
		List<String> tagStringStart = null;
		List<String> tagStringNext = null;
		if ((j - 1 + 2 * i) < childNodes.size()) {
			// get TagString of subTree.
			tagStringStart = new ArrayList<String>();
			tagStringNext = new ArrayList<String>();
			for (int l = j; l < j + i; ++l) {
				node1 = childNodes.get(l);
				node2 = childNodes.get(l + i);
				tagStringStart.addAll(NodeUtils.getFullStringNode(node1));
				tagStringNext.addAll(NodeUtils.getFullStringNode(node2));
			}
			return editDist(tagStringStart, tagStringNext);
		}
		return Float.MAX_VALUE;
	}

	/**
	 * compute tree matching between jth -> (j + i - 1)th childNodes and (j+i)th -> (j + 2 * i)th childNodes
	 * @param childNodes
	 * @param i
	 * @param j
	 * @return percent of matching.
	 */
	public static float distanceTree(List<Node> childNodes, int i, int j) {
		--j;
		if ((j - 1 + 2 * i) < childNodes.size()) {
			List<Node> subChildNodes = childNodes.subList(j, j + 2 * i);
			// get TagString of subTree.
			int counterNode = 0;
			int[][] matching = new int[i + 1][i + 1];
			int tempMatching = 0;
			for (int l = 1; l <= i; ++l) {
				counterNode = counterNode
						+ NodeUtils.getNumNodes(subChildNodes.get(l - 1))
						+ NodeUtils.getNumNodes(subChildNodes.get(l - 1 + i));
				for (int m = 1; m <= i; ++m) {
					matching[l][m] = Math.max(matching[l - 1][m],
							matching[l][m - 1]);
					tempMatching = treeMatching(subChildNodes.get(l - 1),
							subChildNodes.get(i + m - 1));
					matching[l][m] = Math.max(matching[l][m],
							matching[l - 1][m - 1] + tempMatching);
				}
			}
			float percent = (matching[i][i] * 2) / (float) (counterNode);
			counterNode = 2;
			return percent;
		}
		return Float.MIN_VALUE;
	}

	/**
	 * compute tree matching of two given tree.
	 * @param treeSrc
	 * @param treeDes
	 * @return number of matching.
	 */
	public static int treeMatching(Node treeSrc, Node treeDes) {

		if (!treeSrc.getNodeName().equals(treeDes.getNodeName())) {
			return 0;
		}

		// get number of first level sub-tree Src and Des.
		List<Node> subTreeSrc = NodeUtils.getChildrenNode(treeSrc);
		List<Node> subTreeDes = NodeUtils.getChildrenNode(treeDes);
		if (subTreeSrc == null || subTreeDes == null) {
			return 1;
		}
		int k = subTreeSrc.size();
		int n = subTreeDes.size();

		int[][] matching = new int[k + 1][n + 1];
		int tempMatching = 0;
		for (int i = 1; i <= k; ++i) {
			for (int j = 1; j <= n; ++j) {
				matching[i][j] = Math.max(matching[i - 1][j],
						matching[i][j - 1]);
				tempMatching = treeMatching(subTreeSrc.get(i - 1), subTreeDes
						.get(j - 1));
				matching[i][j] = Math.max(matching[i][j],
						matching[i - 1][j - 1] + tempMatching);
			}
		}
		return matching[k][n] + 1;
	}

	public static float normalizeSTM(Node treeSrc, Node treeDes) {
		if (treeSrc == null || treeDes == null) {
			return 0;
		}
		int counterNode = NodeUtils.getNumNodes(treeSrc)
				+ NodeUtils.getNumNodes(treeDes);
		int matching = treeMatching(treeSrc, treeDes);
		float percent = (float) (matching * 2) / counterNode;
		return percent;
	}

	@Deprecated
	public static float editDist(List<String> nodeStringSrc,
			List<String> nodeStringDes) {
		int[][] matching = new int[nodeStringSrc.size() + 1][nodeStringDes
				.size() + 1];
		for (int i = 1; i <= nodeStringSrc.size(); ++i) {
			matching[i][0] = i;
		}
		for (int i = 1; i <= nodeStringDes.size(); ++i) {
			matching[0][i] = i;
		}
		String tagStringSrc = null;
		String tagStringDes = null;
		for (int i = 1; i <= nodeStringSrc.size(); ++i) {
			for (int j = 1; j <= nodeStringDes.size(); ++j) {
				// compute matching p(nodeStringSrc[i], nodeStringDes[j])
				tagStringSrc = nodeStringSrc.get(i - 1);
				tagStringDes = nodeStringDes.get(j - 1);
				int p = tagStringSrc.equals(tagStringDes) ? 0 : 1;
				matching[i][j] = Math.min(matching[i - 1][j - 1] + p,
						matching[i - 1][j] + 1);
				matching[i][j] = Math.min(matching[i][j],
						matching[i][j - 1] + 1);
			}
		}
		float score = matching[nodeStringSrc.size()][nodeStringDes.size()];
		float percent = (score * 2)
				/ (nodeStringSrc.size() + nodeStringDes.size());
		return percent;
	}

	public static void partialTreeAlignment(LinkedList<Node> trees) {
		Collections.sort(trees, new NumNodeComparator());
		Node seedTree = trees.remove();
		List<Node> unalignedTree = new ArrayList<Node>();
		Node tree = null;
		int matching = 0;
		while (!trees.isEmpty()) {
			tree = trees.remove();
			matching = treeMatching(tree, seedTree);
		}
	}
}
