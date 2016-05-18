package com.gy.filewindows.floatwindows;

import java.util.ArrayList;
import java.util.List;

import com.gy.filewindows.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MySpinnerAdapter extends BaseAdapter {
	private String[] items;
	private Context context;
	public MySpinnerAdapter(Context context,String[] items) {
		// TODO Auto-generated constructor stub
		this.items = items;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" }) public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater _LayoutInflater=LayoutInflater.from(context);
        convertView=_LayoutInflater.inflate(R.layout.spinner_item, null);
        if (convertView !=null) {
			TextView textView =(TextView)convertView.findViewById(R.id.itemtext);
			textView.setText(items[position]);
			
		}
        return convertView;
	}
	
}
