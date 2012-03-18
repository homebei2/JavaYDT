package com.wing.ydt.handler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class NormalHander extends DefaultHandler{
	protected short flag=0;
	protected ArrayList<Object> categoryList;
	protected StringBuffer sbf = new StringBuffer();
	public NormalHander(){
		super();
		categoryList = new ArrayList<Object>();
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		if(flag!=0)
		sbf.append(new String(ch,start,length).trim());
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		endParserElement(uri,localName,qName);
		sbf.delete(0, sbf.length());
		flag=0;
	}
	public abstract void endParserElement(String uri, String localName, String qName);
	public abstract void startParserElement(String uri, String localName, String qName);
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		startParserElement(uri,localName,qName);
	}
	public ArrayList<Object> getList(){
		return this.categoryList;
	}
}
