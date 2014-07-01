/**
 * 
 */
package com.solt.algorithm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author thienlong
 *
 */
public class VietNameseCharacter {
	private static Map<Character, Character> specCharMap;
	private static Map<Character, List<Character>> normSpecMap;
	private static List<Character> specSig;
	private static List<Character> specChars;
	private static Set<Character> normChars;
	private static Pattern specPattern;
	static {
		specCharMap = new LinkedHashMap<Character, Character>();
		//special character of 'a'
		//"Ã¡","Ã ","áº¡","áº£","Ã£","Ã¢","áº¥","áº§", "áº­","áº©","áº«","Äƒ","áº¯","áº±","áº·","áº³"
		
		specCharMap.put('à', 'a');
		specCharMap.put('á', 'a');
		specCharMap.put('ả', 'a');
		specCharMap.put('ã', 'a');
		specCharMap.put('ạ', 'a');

		specCharMap.put('â', 'a');
		specCharMap.put('ầ', 'a');
		specCharMap.put('ấ', 'a');
		specCharMap.put('ẩ', 'a');
		specCharMap.put('ẫ', 'a');
		specCharMap.put('ậ', 'a');

		specCharMap.put('ă', 'a');
		specCharMap.put('ằ', 'a');
		specCharMap.put('ắ', 'a');
		specCharMap.put('ẳ', 'a');
		specCharMap.put('ẵ', 'a');
		specCharMap.put('ặ', 'a');


		//special character of 'e'
		//"Ã©","Ã¨","áº¹","áº»","áº½","Ãª","áº¿","á»�" ,"á»‡","á»ƒ","á»…"


		specCharMap.put('è', 'e');
		specCharMap.put('é', 'e');
		specCharMap.put('ẻ', 'e');
		specCharMap.put('ẽ', 'e');
		specCharMap.put('ẹ', 'e');

		specCharMap.put('ê', 'e');
		specCharMap.put('ề', 'e');
		specCharMap.put('ế', 'e');
		specCharMap.put('ể', 'e');
		specCharMap.put('ễ', 'e');
		specCharMap.put('ệ', 'e');

		//special character of 'o'
		//"Ã³","Ã²","á»�","á»�","Ãµ","Ã´","á»‘","á»“", "á»™","á»•","á»—","Æ¡","á»›","á»�","á»£","á»Ÿ"
		
		specCharMap.put('ò', 'o');
		specCharMap.put('ó', 'o');
		specCharMap.put('ỏ', 'o');
		specCharMap.put('õ', 'o');
		specCharMap.put('ọ', 'o');
		
		specCharMap.put('ơ', 'o');
		specCharMap.put('ờ', 'o');
		specCharMap.put('ớ', 'o');
		specCharMap.put('ở', 'o');
		specCharMap.put('ỡ', 'o');
		specCharMap.put('ợ', 'o');
		
		specCharMap.put('ô', 'o');
		specCharMap.put('ồ', 'o');
		specCharMap.put('ố', 'o');
		specCharMap.put('ổ', 'o');
		specCharMap.put('ỗ', 'o');
		specCharMap.put('ộ', 'o');
		
		//special character of 'u'
		//"Ãº","Ã¹","á»¥","á»§","Å©","Æ°","á»©","á»«", "á»±","á»­","á»¯"
		specCharMap.put('ù', 'u');
		specCharMap.put('ú', 'u');
		specCharMap.put('ủ', 'u');
		specCharMap.put('ũ', 'u');
		specCharMap.put('ụ', 'u');
		
		specCharMap.put('ư', 'u');
		specCharMap.put('ừ', 'u');
		specCharMap.put('ứ', 'u');
		specCharMap.put('ử', 'u');
		specCharMap.put('ữ', 'u');
		specCharMap.put('ự', 'u');

		//special character of 'i'
		//"Ã­","Ã¬","á»‹","á»‰","Ä©"
		specCharMap.put('ì', 'i');
		specCharMap.put('í', 'i');
		specCharMap.put('ỉ', 'i');
		specCharMap.put('ĩ', 'i');
		specCharMap.put('ị', 'i');
		specCharMap.put('j', 'i');
		//special character of 'd'
		//"Ä‘"
		specCharMap.put('đ', 'd');

		//special character of 'y'
		//"Ã½","á»³","á»µ","á»·","á»¹"
		specCharMap.put('ỳ', 'y');
		specCharMap.put('ý', 'y');
		specCharMap.put('ỷ', 'y');
		specCharMap.put('ỹ', 'y');
		specCharMap.put('ỵ', 'y');
		
		specChars = new ArrayList<Character>(specCharMap.keySet());
		normChars = new HashSet<Character>(specCharMap.values());
		
		normSpecMap = new HashMap<Character, List<Character>>();
		StringBuilder regexBuilder = new StringBuilder();
		for (Entry<Character, Character> entry : specCharMap.entrySet()) {
			List<Character> specialChar = normSpecMap.get(entry.getValue());
			if (specialChar == null) {
				specialChar = new ArrayList<Character>();
				normSpecMap.put(entry.getValue(), specialChar);
			}
			specialChar.add(entry.getKey());
			regexBuilder.append(entry.getKey()).append('|');
		}
		if (regexBuilder.length() > 0) {
			regexBuilder.deleteCharAt(regexBuilder.length() - 1);
		}
		specPattern = Pattern.compile(regexBuilder.toString());
		specSig = new ArrayList<Character>();
		specSig.add('\u0300');
		specSig.add('\u0301');
		specSig.add('\u0309');
		specSig.add('\u0303');
		specSig.add('\u0323');

	}
	
	public static String getNormalWord(String word) {
		StringBuilder result = new StringBuilder();
		Character c = null;
		for (int i = 0; i < word.length(); ++i) {
			c = specCharMap.get(word.charAt(i));
			if (c != null) {
				result.append(c);
			} else {
				result.append(word.charAt(i));
			}
		}
		return result.toString();
	}
	
	public static boolean isSpecialWord(String word) {
		for (int i = 0; i < word.length(); ++i) {
			if (specChars.contains(word.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSpecChar(char c) {
		return specChars.contains(c);
	}
	
	public static boolean isSpecSig(char c) {
		return specSig.contains(c);
	}
	
	public static char refineSpecChar(char c, char specSig) {
		int i = VietNameseCharacter.specSig.indexOf(specSig);
		if (i == -1) {
			return c;
		}
		int index = 0;
		if (normChars.contains(c)) {
			return VietNameseCharacter.normSpecMap.get(c).get(i);
		} else if ((index = specChars.indexOf(c)) != -1) {
			return VietNameseCharacter.specChars.get(index + i + 1);
		}
		return c;
	}
	
	public static boolean hasSpecialWord(String doc) {
		Matcher matcher = specPattern.matcher(doc);
		return matcher.find();
	}
	
	public static void main(String[] args) {
		System.out.println(hasSpecialWord("xin chÃ o cÃ¡c báº¡n ráº¥t vui Ä‘c lÃ m quen"));
	}
}
