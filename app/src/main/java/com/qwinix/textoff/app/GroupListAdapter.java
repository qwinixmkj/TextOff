package com.qwinix.textoff.app;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GroupListAdapter extends ArrayAdapter<String>{

	private final Activity context;
	private final String[] groups;
	public GroupListAdapter(Activity context,String[] groups)
	{
		super(context, R.layout.rowgrouplayout, groups);
		this.context = context;
		this.groups = groups;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflator = context.getLayoutInflater();
		View rowView = mInflator.inflate(R.layout.rowgrouplayout, null, true);
		TextView mGroupName = (TextView) rowView.findViewById(R.id.groupname);
		mGroupName.setText(groups[position]);
		return rowView;
		
	}
}
