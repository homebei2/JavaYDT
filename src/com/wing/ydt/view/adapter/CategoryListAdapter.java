package com.wing.ydt.view.adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.view.BaseListActivity;
import com.wing.ydt.vo.Category;
import com.wing.ydt.vo.ListItem;
import com.wing.ydt.vo.Message;
import com.wing.ydt.vo.WMessageList;

public class CategoryListAdapter extends BaseListAdapter{
	public CategoryListAdapter(BaseListActivity activity){
		super(activity);
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = reuseIfPossible(convertView,R.id.more_item,R.layout.list_item,null);
		TextView name = (TextView) convertView.findViewById(R.id.m_name);		
		TextView desc = (TextView) convertView.findViewById(R.id.m_desc);			
		ListItem item =   listModel.get(position);
		if(item instanceof Category){
			name.setText(((Category) item).getCategory_name());
			desc.setText(((Category) item).getCategory_desc());
		}else if(item instanceof WMessageList){
			name.setText(((WMessageList) item).getWmessagelist_name());
		}else{
			name.setText(((Message) item).getMessage_name());
			desc.setText(((Message) item).getMessage_body());
		}
		convertView.setTag(item);
		convertView.setOnCreateContextMenuListener(activity);
		convertView.setOnClickListener(activity);
		final int backgroundResource = position % 2 == 0 ? R.drawable.row_background_even : R.drawable.row_background_odd;
		convertView.setBackgroundResource(backgroundResource);
		return convertView;
	}
}
