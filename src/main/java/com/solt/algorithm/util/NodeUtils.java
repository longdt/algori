package com.solt.algorithm.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeUtils {
	private static final String EXCLUDE_REGEX_TAG = "(br)|(script)|(style)";
/*	
	*//**
	 * compute node number of given tree.
	 * @param tree
	 * @return node number.
	 */
	public static int getNumNodes(Node tree) {
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(tree);
		int counter = 1;
		Node node = null;
		List<Node> nodeList = null;
		while (!queue.isEmpty()) {
			node = queue.remove();
			nodeList = getChildrenNode(node);
			if (nodeList == null) {
				continue;
			}
			counter += nodeList.size();
			for (int i = 0, n = nodeList.size(); i < n; ++i) {
					queue.add(nodeList.get(i));
			}
		}
		return counter;
	}

	/**
	 * get children which are visible.
	 * @param tree
	 * @return children of given tree.
	 */
	public static List<Node> getChildrenNode(Node tree) {
		NodeList children = tree.getChildNodes();
		if (children.getLength() == 0) {
			return null;
		}
		List<Node> nodes = new ArrayList<Node>();
		Node node = null;
		for (int i = 0; i < children.getLength(); ++i) {
			node = children.item(i);
			if (isVisible(node)) {
				nodes.add(node);
			}
		}
		return nodes.isEmpty() ? null : nodes;
	}
	
	/**
	 * get the depth of given tree. Ignore text node.
	 * @param tree
	 * @return
	 */
	public static int getTreeDepth(Node tree) {
		if (tree == null) {
			return 0;
		}
		int maxDepth = 0;
		int incrCounterSibling = 0;
		int decrCounterSibling = 1;
		Queue<Node> nodes = new LinkedList<Node>();
		nodes.add(tree);
		while (!nodes.isEmpty()) {
			Node subTree = nodes.poll();
			--decrCounterSibling;
			NodeList subNode = subTree.getChildNodes();
			incrCounterSibling += subNode.getLength();
			for (int i = 0; i < subNode.getLength(); ++i) {
				if (subNode.item(i).getNodeType() == Node.TEXT_NODE) {
					--incrCounterSibling;
				} else {
					nodes.add(subNode.item(i));
				}
			}
			if (decrCounterSibling == 0) {
				++maxDepth;
				decrCounterSibling = incrCounterSibling;
				incrCounterSibling = 0;
			}
		}
		return maxDepth;
	}

	/**
	 * this method is similarity with {@link getFullStringNode} but ignore text node.
	 * @param tree
	 * @return
	 */
	public static List<String> getStringNameNode(Node tree) {
		List<String> nodeString = new ArrayList<String>();
		if (tree != null) {
			Stack<Node> stackNode = new Stack<Node>();
			stackNode.add(tree);
			Node node = null;
			NodeList nodeChildren = null;
			while (!stackNode.isEmpty()) {
				node = stackNode.pop();
				if (node.getNodeType() != Node.TEXT_NODE) {
					nodeString.add(node.getNodeName());
					nodeChildren = node.getChildNodes();
					for (int i = 0; i < nodeChildren.getLength(); ++i) {
						stackNode.add(nodeChildren.item(i));
					}
				}
			}
		}
		return nodeString;
	}
	
	/**
	 * get full string node . This method ignore empty node.
	 * @param tree
	 * @return
	 */
	public static List<String> getFullStringNode(Node tree) {
		List<String> nodeString = new ArrayList<String>();
		if (tree != null) {
			Stack<Node> stackNode = new Stack<Node>();
			stackNode.add(tree);
			Node node = null;
			NodeList nodeChildren = null;
			while (!stackNode.isEmpty()) {
				node = stackNode.pop();
				if (node.getNodeType() != Node.TEXT_NODE) {
					nodeString.add(node.getNodeName());
					nodeChildren = node.getChildNodes();
					for (int i = 0; i < nodeChildren.getLength(); ++i) {
						stackNode.add(nodeChildren.item(i));
					}
				} else if (!isTextNodeEmpty(node)) {
					nodeString.add(node.getNodeName());
				}
			}
		}
		return nodeString;
	}
	
	/**
	 * check whether a given text node is empty
	 * @param textNode
	 * @return
	 */
	public static boolean isTextNodeEmpty(Node textNode) {
		String textContent = textNode.getTextContent().replaceAll("[\\p{Z}\\p{S}\\p{C}]", "");
		return textContent.isEmpty();
	}
	
	/**
	 * get refining text content of a given node.
	 * @param textNode
	 * @return text content
	 */
	private static String getTextContent(Node textNode) {
		return textNode.getTextContent().replaceAll("[\\p{Z}\\p{S}\\p{C}]", " ");
	}
	
	/**
	 * check whether a given node has value.
	 * @param node
	 * @return
	 */
	public static boolean hasValue(Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			return !isTextNodeEmpty(node);
		}
		return true;
	}
	
	/**
	 * check whether a given node is visible
	 * @param node
	 * @return
	 */
	public static boolean isVisible(Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			return !isTextNodeEmpty(node);
		} else if (node.getNodeType() == Node.COMMENT_NODE) {
			return false;
		}
		return !node.getNodeName().matches(EXCLUDE_REGEX_TAG);
	}
	
	/**
	 * Parsing a given html byte[] then output to System.out.
	 * @param htmlBytes
	 * @param htmlEncoding
	 */

	public static String getHtmlMarkup(Node node) {
		StringWriter out = new StringWriter();
		try {
			TransformerFactory.newInstance().newTransformer().transform(
					new DOMSource(node), new StreamResult(out));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return out.toString();
	}

	public static int getNumNodes(List<Node> childNodes, int start, int end) {
		int counter = 0;
		for (int i = start; i < end; ++i) {
			counter += getNumNodes(childNodes.get(i));
		}
		return counter;
	}
	
	public static String refineXPath(String xpath) {
		if (xpath != null) {
			return xpath.replaceAll("tbody/*", "");
		}
		return null;
	}
	
	public static String getContent(Node body) {
		StringBuilder content = new StringBuilder();
		Stack<Node> nodes = new Stack<Node>();
		nodes.add(body);
		Node node = null;
		List<Node> children = null;
		while (!nodes.isEmpty()) {
			node = nodes.pop();
			children = NodeUtils.getChildrenNode(node);
			if (children == null) {
				content.append(node.getTextContent().trim()).append('\n');
			} else {
				nodes.addAll(children);
			}
		}
		return content.toString();
	}
}
