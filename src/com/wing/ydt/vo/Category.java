package com.wing.ydt.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class Category extends ListItem implements Parcelable {
	private int category_type;
	private String category_name;
	private String category_desc;
	public int getCategory_type() {
		return category_type;
	}
	public void setCategory_type(int categoryType) {
		category_type = categoryType;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String categoryName) {
		category_name = categoryName;
	}
	public String getCategory_desc() {
		return category_desc;
	}
	public void setCategory_desc(String categoryDesc) {
		category_desc = categoryDesc;
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(category_type);
		dest.writeString(category_name);
		dest.writeString(category_desc);
	}
	 public void readFromParcel(Parcel in) {
			this.category_type=in.readInt();
			this.category_name=in.readString();
			this.category_desc=in.readString();
	 }
	 public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
	        public Category createFromParcel(Parcel in) {
	            return new Category(in);
	        }

	        public Category[] newArray(int size) {
	            return new Category[size];
	        }
	    };
	    
	    private Category(Parcel in) {
	        readFromParcel(in);
	    }
	    public Category(){
	    	
	    }
		@Override
		public int getId() {
			// TODO Auto-generated method stub
			return category_type;
		}
}
