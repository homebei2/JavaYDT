package com.wing.ydt.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable{
	private int message_id;
	private int category_type;
	private String message_name;
	private String message_body;
	private String message_desc;
	public int getMessage_id() {
		return message_id;
	}
	public void setMessage_id(int messageId) {
		message_id = messageId;
	}
	public int getCategory_type() {
		return category_type;
	}
	public void setCategory_type(int categoryType) {
		category_type = categoryType;
	}
	public String getMessage_name() {
		return message_name;
	}
	public void setMessage_name(String messageName) {
		message_name = messageName;
	}
	public String getMessage_body() {
		return message_body;
	}
	public void setMessage_body(String messageBody) {
		message_body = messageBody;
	}
	public String getMessage_desc() {
		return message_desc;
	}
	public void setMessage_desc(String messageDesc) {
		message_desc = messageDesc;
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
    
    private Message(Parcel in) {
        readFromParcel(in);
    }
	private void readFromParcel(Parcel in) {
		// TODO Auto-generated method stub
		this.message_id=in.readInt();
		this.message_name=in.readString();
		this.message_body=in.readString();
		this.message_desc=in.readString();
		this.category_type=in.readInt();
	}
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(message_id);
		dest.writeString(message_name);
		dest.writeString(message_body);
		dest.writeString(message_desc);
		dest.writeInt(category_type);
	} 
	public Message(){
		
	}
}
