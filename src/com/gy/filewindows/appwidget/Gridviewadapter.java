package com.gy.filewindows.appwidget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gy.fileserver.GetFileProperty;
import com.gy.filewindows.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Gridviewadapter extends BaseAdapter {
	private Context mContext;
	private List<String> filelist = new ArrayList<String>();

	public Gridviewadapter(Context context, List<String> tmpfilelist) {
		// TODO Auto-generated constructor stub
		mContext = context;
		filelist = tmpfilelist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filelist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return filelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		File f = new File(filelist.get(position).toString());
		View view = View.inflate(mContext, R.layout.gridview, null);
		TextView title = (TextView) view.findViewById(R.id.name);
		title.setText(f.getName());
		ImageView imageView = (ImageView) view.findViewById(R.id.coverimg);
		imageView.setImageResource(GetFileProperty.getfileicon(GetFileProperty.getfileopenkind(f)));
		return view;
	}
}
