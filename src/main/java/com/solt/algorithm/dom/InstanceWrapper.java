package com.solt.algorithm.dom;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class InstanceWrapper {
	private static String[] itemsName;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public static void ide(Collection<List<Node>> pages, byte numTokens) {
		Iterator<List<Node>> iterator = pages.iterator();
		List<Node> page = iterator.next();
		Set<Template> templates = new HashSet<Template>();
		labelPage(templates, page, numTokens);
		while (iterator.hasNext()) {
			page = iterator.next();
			if (!extract(templates, page)) {
				labelPage(templates, page, numTokens);
			}
		}
	}

	private static boolean extract(Set<Template> templates, List<Node> page) {
		for (Template temp : templates) {
			if (extractItems(temp, page, 1, page.size())) {
				// output all extracted items from page.
				return true;
			}
		}
		return false;
	}

	public static boolean extractItems(Template temp, List<Node> page, int start, int end) {
		Collection<Item> items = temp.getItems();
		Map<Item, int[]> match = new HashMap<Item, int[]>();
		for (int i = start; i < end; ++i) {
			for (Item item : items) {
				if (!item.isMissing()) {
					//coordinate present matching prefix. coordinate[0] present score matching.
					//coordinate[1] present place matching and coordinate[2] present number matching.
					int[] curCoordinate = new int[3];
					curCoordinate[0] = numMatch(page, i, item.getPrefix());
					int[] finalCoordinate = match.get(item);
					if (finalCoordinate[0] == curCoordinate[0]) {
						++finalCoordinate[2];
					} else if (finalCoordinate == null || finalCoordinate[0] < curCoordinate[0]) {
						curCoordinate[1] = i;
						curCoordinate[2] = 1;
						match.put(item, curCoordinate);
					}
				}
			}
		}
		
		//get item which can be uniquely identified.
		Item uniqueItem = null;
		int startItem = 0;
		for (Entry<Item, int[]> entry : match.entrySet()) {
			int[] coordinate = entry.getValue();
			if (coordinate[2] == 1) {
				uniqueItem = entry.getKey();
				startItem = coordinate[1];
			}
		}
		
	/*	if (uniqueItem != null) {
			
			} else if () {
			
			} else {
			return false;
			}*/
		return true;
	}
	
	/*public static int getEndOfItem() {
		for (int i = start; i < end; ++i) {
			for (Item item : items) {
				if (!item.isMissing()) {
					//coordinate present matching prefix. coordinate[0] present score matching.
					//coordinate[1] present place matching and coordinate[2] present number matching.
					int[] curCoordinate = new int[3];
					curCoordinate[0] = numMatch(page, i, item.getPrefix());
					int[] finalCoordinate = match.get(item);
					if (finalCoordinate[0] == curCoordinate[0]) {
						++finalCoordinate[2];
					} else if (finalCoordinate == null || finalCoordinate[0] < curCoordinate[0]) {
						curCoordinate[1] = i;
						curCoordinate[2] = 1;
						match.put(item, curCoordinate);
					}
				}
			}
		}
	}*/

	public static int numMatch(List<Node> page, int i, String matchString) {
		int score = 0;
		String tagName = page.get(i).getNodeName();
		while (matchString.startsWith(tagName)) {
			++score;
			matchString.substring(tagName.length());
			--i;
			tagName = page.get(i).getNodeName();
		}
		return score;
	}

	private static void labelPage(Set<Template> templates, List<Node> page,
			byte numTokens) {
		Template temp = new Template();
		Scanner in = new Scanner(System.in);
		String line = null;
		Item item = null;
		for (String itemName : itemsName) {
			item = new Item();
			item.setItemName(itemName);
			System.out.print("Enter prefix for item (enter 'q' for no input) "
					+ itemName + ": ");
			line = in.nextLine();
			if (line.equals('q')) {
				continue;
			} else {
				item.setPrefix(line);
			}
			System.out.print("Enter prefix for item " + itemName + ": ");
			line = in.nextLine();
			item.setSuffix(line);
			temp.addItem(item);
		}
		templates.add(temp);
	}
}
