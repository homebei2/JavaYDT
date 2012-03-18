package com.wing.ydt.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Log;

public class WebServiceCall {
	private final static String TAG = "com.wing.wp.db.WebServiceCall";

	public static void invokeWithTimeout(String urlString,
			DefaultHandler handler, int defaultTimeout) {
		HttpURLConnection conn = null;
		InputStream in = null;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setInstanceFollowRedirects(false);
			
			if (defaultTimeout > 0) {
				conn.setConnectTimeout(defaultTimeout);
				conn.setReadTimeout(defaultTimeout);
			}
			in = conn.getInputStream();
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parse = spf.newSAXParser();
			parse.parse(in, handler);
		} catch (Exception e) {
			if (e.getMessage() != null) {
				Log.i(TAG, "InputStream error!" + e.getMessage());
			}
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	public static void invokeWithTimeout(InputStream in,
			DefaultHandler handler, int defaultTimeout) {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parse = spf.newSAXParser();
			parse.parse(in, handler);
		} catch (Exception e) {
			if (e.getMessage() != null) {
				Log.i(TAG, "InputStream error!" + e.getMessage());
			}
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}


}
