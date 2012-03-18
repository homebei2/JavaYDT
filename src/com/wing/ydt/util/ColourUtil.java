package com.wing.ydt.util;

import com.wing.ydt.Application;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class ColourUtil {
	
	public static ColourUtil INSTANCE = new ColourUtil();
	
	private ColourUtil() {
	}
	
	public void setColourSpan(Activity activity, int textViewId, int stringId, int colourId, String substring) {
			TextView textView = (TextView) activity.findViewById(textViewId);
			String text = activity.getString(stringId);
			setColourSpan(activity,  textView,  text,  colourId,  substring); 
			}
	public void setColourSpan(Activity activity, TextView textView, String string, int colourId, String substring) {
		textView.setText(string, TextView.BufferType.SPANNABLE);
		
		Spannable spannable = (Spannable) textView.getText();
		int startIndex = string.indexOf(substring);
		if(startIndex==-1){
			startIndex=0;
		}
		int endIndex = startIndex + substring.length();
    	spannable.setSpan(new ForegroundColorSpan(activity.getResources().getColor(colourId)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	/**
	 * This method will highlight words from the stringIDArray using the colourID given
	 * @param textView The TextView that will be used to retrieve the text to be highlighted
	 * @param stringIDArray List of String IDs to highlight
	 * @param colourID colourID to be used for highlighting
	 */
	public void highlightText(TextView textView, int[] stringIdArray, int colourId) {
		String[] strings = new String[stringIdArray.length];
		for (int i = 0; i < stringIdArray.length; i++) {
			strings[i] = textView.getResources().getString(stringIdArray[i]);
		}
		
		highlightText(textView, strings, colourId);
	}
	
	public void highlightText(TextView textView, int stringArrayId, int colourId) {
		String[] strings = textView.getResources().getStringArray(stringArrayId);
		highlightText(textView, strings, colourId);
	}
	
	public void highlightText(TextView textView, String[] strings, int colourId) {
		CharSequence text = textView.getText();
		int color = textView.getResources().getColor(colourId);
		
		SpannableString spannedString = new SpannableString(text);
		String string = spannedString.toString();
		
		for (String specialWord : strings) {
			int index = string.indexOf(specialWord);
			if (index > -1) {
				spannedString.setSpan(new ForegroundColorSpan(color), index, index + specialWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		
		textView.setText(spannedString, BufferType.SPANNABLE);
	}
	
	public String getColorAsString(int colorId) {
		return getColorAsString(colorId, false);
	}
	
	public String getColorAsString(int colorId, boolean keepAlphaValues) {
		int color = Application.getContext().getResources().getColor(colorId);
		String result = String.format("%8x", color);
		if (!keepAlphaValues) {
			result = result.substring(2);
		}
		result = "#" + result;
		return result;
	}
	
}