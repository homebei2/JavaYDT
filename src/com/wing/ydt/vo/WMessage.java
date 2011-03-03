package com.wing.ydt.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class WMessage implements Parcelable{
	private int wmessage_id;
	private int wmessagelist_id;
	private String wmessage_name;
	private String wmessage_body;
	private String wmessage_answer;
	public int getMessage_id() {
		return wmessage_id;
	}
	
	public int getWmessage_id() {
		return wmessage_id;
	}

	public void setWmessage_id(int wmessageId) {
		wmessage_id = wmessageId;
	}

	public int getWmessagelist_id() {
		return wmessagelist_id;
	}

	public void setWmessagelist_id(int wmessagelistId) {
		wmessagelist_id = wmessagelistId;
	}

	public String getWmessage_name() {
		return wmessage_name;
	}

	public void setWmessage_name(String wmessageName) {
		wmessage_name = wmessageName;
	}

	public String getWmessage_body() {
		return wmessage_body;
	}

	public void setWmessage_body(String messageBody) {
		wmessage_body = messageBody;
	}

	public String getWmessage_answer() {
		return wmessage_answer;
	}

	public void setWmessage_answer(String wmessageAnswer) {
		wmessage_answer = wmessageAnswer;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public static final Parcelable.Creator<WMessage> CREATOR = new Parcelable.Creator<WMessage>() {
        public WMessage createFromParcel(Parcel in) {
            return new WMessage(in);
        }

        public WMessage[] newArray(int size) {
            return new WMessage[size];
        }
    };
    
    private WMessage(Parcel in) {
        readFromParcel(in);
    }
	private void readFromParcel(Parcel in) {
		// TODO Auto-generated method stub
		this.wmessage_id=in.readInt();
		this.wmessage_name=in.readString();
		this.wmessage_body=in.readString();
		this.wmessage_answer=in.readString();
		this.wmessagelist_id=in.readInt();
	}
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(wmessage_id);
		dest.writeString(wmessage_name);
		dest.writeString(wmessage_body);
		dest.writeString(wmessage_answer);
		dest.writeInt(wmessagelist_id);
	} 
	public WMessage(){
		
	}
}
