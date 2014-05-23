package com.brandon.botes;

import java.util.HashMap;
import java.util.Map;

public class IconBank {
	public  static final Map<String, Integer> icons;
    static
    {
    	icons = new HashMap<String, Integer>();
    	icons.put("iron man", R.drawable.ironmanorig);
    	icons.put("iron man silver", R.drawable.ironmansilver);
    	icons.put("minion", R.drawable.minion);
    	icons.put("pepper", R.drawable.pepper);
    	icons.put("orange", R.drawable.orange);
    	icons.put("yellow pepper", R.drawable.yellowpepper);
    	icons.put("squash", R.drawable.shash);
    	icons.put("egg", R.drawable.egg);
    }
}
