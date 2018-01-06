package de.frittenburger.aylive.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Cache {

	private final Set<String> set = new HashSet<String>();
	private final File file;

	public Cache(String filename) {
		this.file = new File(filename);
		
		if(!file.exists()) return;
		
		BufferedReader in = null;
		try {
			 in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
			 String readLine;
			 while ((readLine = in.readLine()) != null) {
				 if(readLine.startsWith("#")) continue;
			     set.add(readLine.trim());
			 }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public boolean exists(String id) {
		return set.contains(id);
	}

	public void add(String id) {
		
		if(set.contains(id)) 
			throw new IllegalArgumentException();
		
		set.add(id);
		
		PrintWriter out = null;
		try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
				out.println(id);
		} catch (IOException e) {
				e.printStackTrace();
		} finally {
			if(out != null)
				out.close();
		}
		 
	}
	
	private static Map<String,Cache> cacheMap = new HashMap<String,Cache>();
	public static synchronized Cache getInstance(String key) {
		if(!cacheMap.containsKey(key))
			cacheMap.put(key,new Cache("cache/"+key+".txt"));
		return cacheMap.get(key);
	}

}
