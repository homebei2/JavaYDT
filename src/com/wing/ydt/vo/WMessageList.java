package com.wing.ydt.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class WMessageList extends ListItem implements Parcelable{
	private int category_type;
	private int wmessagelist_id;
	private String wmessagelist_name;
	private String wmessagelist_desc;
	public String getWmessagelist_desc() {
		return wmessagelist_desc;
	}
	public void setWmessagelist_desc(String wmessagelistDesc) {
		wmessagelist_desc = wmessagelistDesc;
	}
	public int getCategory_type() {
		return category_type;
	}
	public void setCategory_type(int categoryType) {
		category_type = categoryType;
	}
	public int getWmessagelist_id() {
		return wmessagelist_id;
	}
	public void setWmessagelist_id(int wmessagelistId) {
		wmessagelist_id = wmessagelistId;
	}
	public String getWmessagelist_name() {
		return wmessagelist_name;
	}
	public void setWmessagelist_name(String wmessagelistName) {
		wmessagelist_name = wmessagelistName;
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(category_type);
		dest.writeInt(wmessagelist_id);
		dest.writeString(wmessagelist_name);
		dest.writeString(wmessagelist_desc);
	}
	 public void readFromParcel(Parcel in) {
			this.category_type=in.readInt();
			this.wmessagelist_id=in.readInt();
			this.wmessagelist_name=in.readString();
			wmessagelist_desc=in.readString();
	 }
	 public static final Parcelable.Creator<WMessageList> CREATOR = new Parcelable.Creator<WMessageList>() {
	        public WMessageList createFromParcel(Parcel in) {
	            return new WMessageList(in);
	        }

	        public WMessageList[] newArray(int size) {
	            return new WMessageList[size];
	        }
	    };
	    
	    private WMessageList(Parcel in) {
	        readFromParcel(in);
	    }
	    public WMessageList(){
	    	
	    }
		@Override
		public int getId() {
			// TODO Auto-generated method stub
			return wmessagelist_id;
		}
}
