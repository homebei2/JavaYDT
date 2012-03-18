package com.wing.ydt.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wing.ydt.Application;
import com.wing.ydt.handler.UIFactory;
import com.wing.ydt.view.BaseListActivity;
import com.wing.ydt.vo.ListItem;


public abstract class BaseListAdapter extends BaseAdapter{
	
	protected List<ListItem> listModel;
	protected Runnable onEmptyAction;
	protected Runnable onNonEmptyAction;
	BaseListActivity activity;
	public BaseListAdapter(BaseListActivity activity) {
		super();
		this.activity = activity;
		listModel = new ArrayList<ListItem>();
	}

	@Override
	public int getCount() {
		return listModel.size();
	}

	@Override
	public Object getItem(int position) {
		if (position >= listModel.size() || position < 0)
			return null;
		return listModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (position >= listModel.size())
			return position;
		return listModel.get(position).getId();
	}
	
	public ListItem getItemById(int id) {
		for (ListItem item : listModel) {
			if (item.getId()==id) {
				return item;
			}
		}
		return null;
	}
	
	public List<ListItem> getItems() {
		return listModel;
	}
	
	public void setItems(List<ListItem> list) {
		boolean wasEmpty = listModel.size() == 0;
		listModel = list;
		if ((listModel.size() == 0) && (onEmptyAction != null)) {
			onEmptyAction.run();
		} else if (wasEmpty && listModel.size() > 0 && onNonEmptyAction != null) {
			onNonEmptyAction.run();
		}
		notifyDataSetChanged();
	}
	
	public void add(ListItem item) {
		listModel.add(item);
		if (listModel.size() == 1 && onNonEmptyAction != null) {
			onNonEmptyAction.run();
		}
		notifyDataSetChanged();
	}
	
	public void add(int index, ListItem item) {
		listModel.add(index, item);
		if (listModel.size() == 1 && onNonEmptyAction != null) {
			onNonEmptyAction.run();
		}
		notifyDataSetChanged();
	}
	public void changeItem(ListItem it) {
		for (ListItem item : listModel) {
			if (item.getId()==it.getId()) {
				listModel.set(listModel.indexOf(item),it);
				break;
			}
		}
		notifyDataSetChanged();
	}
	public void removeItemByID(int itemId) {
		for (ListItem item : listModel) {
			if (item.getId()==itemId) {
				removeItem(item);
				break;
			}
		}
	}
	
	public void removeItem(ListItem item) {
		listModel.remove(item);
		if ((listModel.size() == 0) && (onEmptyAction != null)) {
			onEmptyAction.run();
		}
		notifyDataSetChanged();
	}
	
	public void clear() {
		listModel.clear();
		if (onEmptyAction != null) {
			onEmptyAction.run();
		}
		notifyDataSetChanged();
	}
	
	protected View reuseIfPossible(View possiblyReusableView, int uniqueIdToIdentifyReuse, int layoutId, ViewGroup parent) {
		if ((possiblyReusableView == null || possiblyReusableView.findViewById(uniqueIdToIdentifyReuse) == null)) {
			possiblyReusableView = UIFactory.instance.inflate(layoutId);
		}
		return possiblyReusableView;
	}
	public void setOnEmptyAction(Runnable onEmptyAction) {
		this.onEmptyAction = onEmptyAction;
	}
	
	public void setOnNonEmptyAction(Runnable onNonEmptyAction) {
		this.onNonEmptyAction = onNonEmptyAction;
	}
}