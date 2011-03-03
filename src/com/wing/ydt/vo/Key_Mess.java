package com.wing.ydt.vo;

public class Key_Mess {
	private int keyword_type;
	private int message_id;
	private String keyword_name;
	public String getKeyword_name() {
		return keyword_name;
	}
	public void setKeyword_name(String keywordName) {
		keyword_name = keywordName;
	}
	private String key_mess_desc;
	public int getKeyword_type() {
		return keyword_type;
	}
	public void setKeyword_type(int keywordType) {
		keyword_type = keywordType;
	}
	public int getMessage_id() {
		return message_id;
	}
	public void setMessage_id(int messageId) {
		message_id = messageId;
	}
	public String getKey_mess_desc() {
		return key_mess_desc;
	}
	public void setKey_mess_desc(String keyMessDesc) {
		key_mess_desc = keyMessDesc;
	}

}
