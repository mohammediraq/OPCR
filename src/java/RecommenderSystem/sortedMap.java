/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecommenderSystem;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author AhmedShalaby
 */
public class sortedMap {
    
    public static void main (String args[])
    {
      
	}
 
	public static Map sortByValue(Map unsortedMap) {
		Map sortedMap = new TreeMap((Comparator) new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}
 
}
 
class ValueComparator implements Comparator {
	Map map;
 
	public ValueComparator(Map map) {
		this.map = map;
	}
 
	public int compare(Object keyA, Object keyB) {
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		return valueB.compareTo(valueA);
	}
    }
    

