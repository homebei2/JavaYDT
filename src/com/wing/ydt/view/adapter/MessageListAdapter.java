package com.wing.ydt.view.adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wing.ydt.R;
import com.wing.ydt.view.BaseListActivity;
import com.wing.ydt.vo.Message;

public class MessageListAdapter extends BaseListAdapter {
	public MessageListAdapter(BaseListActivity activity){
		super(activity);
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view =  reuseIfPossible(convertView,R.id.more_item,R.layout.list_item,null);
		
		Message cat =  (Message) listModel.get(position);
		((TextView)view.findViewById(R.id.m_name)).setText(cat.getMessage_name());
		((TextView)view.findViewById(R.id.m_desc)).setText(cat.getMessage_body());
		return view;
	}
}
