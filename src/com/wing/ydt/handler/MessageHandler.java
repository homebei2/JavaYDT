package com.wing.ydt.handler;
import android.widget.TextView;

import com.wing.ydt.Application;
import com.wing.ydt.util.StringUtil;
import com.wing.ydt.vo.Message;

public class MessageHandler extends NormalHander{		
		private Message message;
		private TextView tv  = new TextView(Application.getContext());
		public MessageHandler(){
			super();
		}
		@Override
		public void endParserElement(String uri, String localName, String qName) {
			// TODO Auto-generated method stub
			switch(flag){
			case 1:
				break;
			case 2:
				message.setMessagelist_id(Integer.parseInt(sbf.toString().trim()));
				break;
			case 3:
				tv.setText(StringUtil.format(sbf.toString().trim()));
				message.setMessage_name(tv.getText().toString());
//				message.setMessage_name(sbf.toString().trim());
				break;
			case 4:
				message.setCategory_type(Integer.parseInt(sbf.toString().trim()));
				break;
			case 5:
				tv.setText(StringUtil.format(sbf.toString().trim()));
				message.setMessage_body(tv.getText().toString());
//				message.setMessage_body(sbf.toString().trim());
				break;
			case 6:
				tv.setText(StringUtil.format(sbf.toString().trim()));
				message.setMessage_answer(tv.getText().toString());
				categoryList.add(message);
				break;
			}
		}

		@Override
		public void startParserElement(String uri, String localName,
				String qName) {
			// TODO Auto-generated method stub
			if("message".equals(localName)){
				message = new Message();
				flag=1;
			}else if("list_id".equals(localName)){
				flag=2;
			}else if("name".equals(localName))
				flag=3;
			else if("type".equals(localName))
				flag=4;
			else if("body".equals(localName))
				flag=5;
			else if("desc".equals(localName))
				flag=6;
			else if("answer".equals(localName))
				flag=6;
			else
				flag=0;
		}
	}