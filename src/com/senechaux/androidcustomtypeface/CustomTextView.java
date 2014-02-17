package com.senechaux.androidcustomtypeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {
	public CustomTextView(Context context) {
		super(context);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context, attrs);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		parseAttributes(context, attrs);
	}

	private void parseAttributes(Context context, AttributeSet attrs) {
		if (!isInEditMode()) {
			TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

			String fontName = values.getString(R.styleable.CustomTextView_typeface);

			Typeface tf = CustomTypeface.getInstance(context).getTypeface(fontName);

			setTypeface(tf);
		}
	}

}
