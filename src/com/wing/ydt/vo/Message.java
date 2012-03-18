package com.wing.ydt.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class Message extends ListItem implements Parcelable{
	private int message_id;
	private int category_type;
	private int messagelist_id;
	private int favorite;
	public boolean isFavorite() {
		return favorite==1;
	}
	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	public int getMessagelist_id() {
		return messagelist_id;
	}
	public void setMessagelist_id(int messagelistId) {
		messagelist_id = messagelistId;
	}
	private String message_name;
	private String message_body;
	private String message_answer;
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
		this.messagelist_id=in.readInt();
		this.message_name=in.readString();
		this.message_body=in.readString();
		this.message_answer=in.readString();
		this.category_type=in.readInt();
		this.favorite=in.readInt();
	}
	public String getMessage_answer() {
		return message_answer;
	}
	public void setMessage_answer(String message_answer) {
		this.message_answer = message_answer;
	}
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(message_id);
		dest.writeInt(messagelist_id);
		dest.writeString(message_name);
		dest.writeString(message_body);
		dest.writeString(message_answer);
		dest.writeInt(category_type);
		dest.writeInt(favorite);
	} 
	public Message(){
		
	}
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return message_id;
	}
}
