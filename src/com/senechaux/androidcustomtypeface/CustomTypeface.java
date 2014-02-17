package com.senechaux.androidcustomtypeface;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;

public class CustomTypeface {
	private static CustomTypeface mInstance = null;
	private Context context;
	private static Hashtable<String, Typeface> typefacesCache;

	private CustomTypeface(Context c) {
		context = c;
		createTypefaces();
	}

	public static CustomTypeface getInstance(Context c) {
		if (mInstance == null) {
			mInstance = new CustomTypeface(c);
		}
		return mInstance;
	}

	private void createTypefaces() {
		typefacesCache = new Hashtable<String, Typeface>();

		String[] fontsNames = context.getResources().getStringArray(R.array.fonts_names);
		for (String fontName : fontsNames) {
			typefacesCache.put(
					fontName,
					Typeface.createFromAsset(context.getAssets(),
							String.format("fonts/%s.ttf", fontName)));
		}

	}

	public Typeface getTypeface(String name) {
		synchronized (typefacesCache) {
			Typeface tf = Typeface.DEFAULT;
			if (null != name && typefacesCache.containsKey(name)) {
				tf = typefacesCache.get(name);
			}
			return tf;
		}
	}

}