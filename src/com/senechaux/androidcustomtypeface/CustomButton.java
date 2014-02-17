package com.senechaux.androidcustomtypeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {
	public CustomButton(Context context) {
		super(context);
	}

	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context, attrs);
	}

	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		parseAttributes(context, attrs);
	}

	private void parseAttributes(Context context, AttributeSet attrs) {
		if (!isInEditMode()) {
			TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);

			String fontName = values.getString(R.styleable.CustomButton_typeface);

			Typeface tf = CustomTypeface.getInstance(context).getTypeface(fontName);

			setTypeface(tf);
		}
	}
}