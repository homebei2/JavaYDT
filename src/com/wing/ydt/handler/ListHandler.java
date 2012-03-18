package com.wing.ydt.handler;
import com.wing.ydt.vo.WMessageList;
public class ListHandler extends NormalHander{
	private WMessageList message;
	public ListHandler(){
		super();
	}
	@Override
	public void endParserElement(String uri, String localName, String qName) {
		// TODO Auto-generated method stub
		switch(flag){
		case 1:
			break;
		case 2:
			message.setWmessagelist_id(Integer.parseInt(sbf.toString().trim()));
			break;
		case 3:
			message.setWmessagelist_name(sbf.toString().trim());
			break;
		case 4:
			message.setCategory_type(Integer.parseInt(sbf.toString().trim()));
			break;
		case 5:
			message.setWmessagelist_desc(sbf.toString().trim());
			categoryList.add(message);
			break;
		}
	}
	@Override
	public void startParserElement(String uri, String localName, String qName) {
		// TODO Auto-generated method stub
		if("wmessagelist".equals(localName)){
			message = new WMessageList();
		}else if("id".equals(localName)){
			flag=2;
		}else if("name".equals(localName))
			flag=3;
		else if("type".equals(localName))
			flag=4;
		else if("desc".equals(localName))
			flag=5;
		else
			flag=0;
	}
}
