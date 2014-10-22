package com.joymeter.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class Util {
	
	public static <T> HashSet<T> cloneHashSet(HashSet<T> hashSet) {
		return (HashSet<T>)hashSet.clone();
	}
	
	public static <T> Collection<T> cloneCollection(Collection<T> collection) {
		return (Collection<T>)new LinkedList<T>(collection);
	}
}
