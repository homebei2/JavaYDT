package com.wing.ydt.util;

import java.util.Date;

import android.graphics.Paint;
import android.util.Log;

public class StringUtil {

	public static String convertStr(String str) {
		boolean _pos = false;
		String str1 = "";
		String str2;
		int len = str.length();
		for (int i = 0; i < len; i++) {
			if (str.substring(i, i + 1).equals(".")) {
				if (!_pos) {
					str1 += str.substring(i, i + 1);
					_pos = true;
				}
			} else {
				str1 += str.substring(i, i + 1);
			}
		}
		str2 = str1.trim();
		return str2;
	}

	public static String userSubString(String str, char dot) {
		String resultStr = null;
		StringBuffer midString = new StringBuffer();

		char[] _userCharArr = str.toCharArray();
		int num = _userCharArr.length;
		for (int i = 0; i < num; i++) {
			if (_userCharArr[i] == dot) {
				midString = midString.append(_userCharArr[i]);
				resultStr = midString.toString();
				break;
			} else {
				midString = midString.append(_userCharArr[i]);
			}
		}
		return resultStr;
	}

	public static String subStr(String str, char ch) {
		if (str == null) {
			return "";
		}
		int i = str.indexOf(ch);
		String outStr;
		if (i > 0) {
			outStr = str.substring(0, (str.indexOf(ch))).trim();
		} else {
			outStr = str;
		}
		return outStr;
	}

	public static String limitedStr2(String str, int charTotal) {
		String[] args = str.split(" |/");
		StringBuffer sbuf = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			if (sbuf.length() <= charTotal) {
				sbuf.append(args[i]);
				if (i < args.length - 1) {
					sbuf.append(" ");
				}
				if(i<args.length-1){
					if ((sbuf.length() + args[i+1].length()) > charTotal) {
						if(str.length()>sbuf.length()){
							sbuf.append("...");
						}					
						break;
					} 
				}else{
					if ((sbuf.length() + args[args.length-1].length()) > charTotal) {
						if(str.length()>sbuf.length()){
							sbuf.append("...");
						}					
						break;
					} 
				}
			}
		}
		return sbuf.toString().trim();
	}

	public static String limitedDisplayStr2(String str, float width, Paint mPint) {
		int charTotal = adjustCharSpace(mPint,width);
		//float charTotal = (width / perCharWidth);
		String temp = limitedStr2(str, charTotal);
		// Log.i("StringUtil", temp);
		// if(str.length()>temp.length()){
		// if(temp.length()>(charTotal-3)){
		// temp=str.substring(0,charTotal-3);
		// }
		// temp+="...";
		// }
		return temp;
	}

	public static String limitedStr(String str,int charTotal){
		String[] args=str.split(" |/");
		int totalLen=0;
		StringBuffer sbuf=new StringBuffer();
		for(int i=0;i<args.length;i++){
			totalLen+=args[i].length();
			if(totalLen<=charTotal){
				sbuf.append(args[i]);
			}
			if(i<args.length-1){
				sbuf.append(" ");
				totalLen++;
			}
		}
		return sbuf.toString().trim();
	}
	
	public static String limitedDisplayStr(String str,float width,Paint mPint){
		float perCharWidth=getCharSpace(mPint);
		int charTotal=(int) (width/perCharWidth);
		String temp=limitedStr(str,charTotal);
		if(str.length()>temp.length()){
			if(temp.length()>(charTotal-3)){
				temp=str.substring(0,charTotal-3);
			}
			temp+="...";
		}
		return temp;
	}
	
	
	
	public static int adjustCharSpace(Paint mPint,float width) {
		//float fontSpace = mPint.getFontSpacing()/2;
		float charTotal = (width * 2.3f/  mPint.getFontSpacing());
		return (int)charTotal;
	}
	
	public static float getCharSpace(Paint mPint) {
		float fontSpace = mPint.getFontSpacing()/2;
		return fontSpace;
	}
	/**
	 * replace char to string
	 * 
	 * @param sourceStr
	 * @param targetChar
	 * @param replacement
	 * @return
	 */
	public static String replaceAll(String sourceStr, char targetChar,
			String replacement) {
		StringBuffer buf = new StringBuffer();
		int beginIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < sourceStr.length(); i++) {
			if (sourceStr.charAt(i) == targetChar) {
				endIndex = i;
				buf.append(sourceStr, beginIndex, (endIndex - beginIndex));
				buf.append(replacement, 0, replacement.length());
				beginIndex = i + 1;

			}
		}
		buf.append(sourceStr, beginIndex, sourceStr.length() - beginIndex);
		return buf.toString();
	}

	public static String replaceURLSpace(String sourceStr) {
		return replaceAll(sourceStr, ' ', "%20");
	}

	public static String getDateLocalString() {
		Date date = new Date();
		return date.toLocaleString();
	}

	public static boolean isURLStr(String inStr) {
		if (inStr != null && inStr.trim().length() != 0
				&& inStr.toLowerCase().contains("http")) {
			return true;
		} else {
			return false;
		}
	}
}
