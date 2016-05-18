package com.gy.widget.listview;

import java.io.File;
import java.util.List;

import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.CheckBox.OnCheckListener;
import com.gy.fileserver.GetFileProperty;
import com.gy.filewindows.MainActivity;
import com.gy.filewindows.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Filelistadapter extends BaseAdapter {
	private LayoutInflater inflater;
	// private Bitmap mIcon1;
	private Bitmap mIcon2;
	//private Bitmap mIcon3;
	//private Bitmap mIcon4;
	private List<String> items;
	private List<String> paths;
	private Context mContext;

	public Filelistadapter(Context context, List<String> it, List<String> pa) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		items = it;
		paths = pa;
		// mIcon1 = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.file);
		mIcon2 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.app_icon);
	//	mIcon3 = BitmapFactory.decodeResource(context.getResources(),
	//			R.drawable.folder);
		//mIcon4 = BitmapFactory.decodeResource(context.getResources(),
		//		iconselect(getMIMEType(f)));
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.filelistlayout, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		File f = new File(paths.get(position).toString());
		if (items.get(position).toString().equals("back")) {
			holder.text.setText("Back to ..");
			holder.icon.setImageBitmap(mIcon2);
			holder.checkBox.setVisibility(View.GONE);
		} else {
			if (MainActivity.isshowck) {
				holder.checkBox.setVisibility(View.VISIBLE);
				setcheckboxlistener(holder.checkBox, position);
			}
			holder.text.setText(f.getName());
			if (f.isDirectory()) {
				holder.icon.setImageBitmap(mIcon2);
			} else {
				holder.icon.setImageResource(iconselect(getMIMEType(f)));
				//holder.icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
				//		iconselect(getMIMEType(f))));
			}
		}
		return convertView;

	}
	private int iconselect(String type) {
		return GetFileProperty.getfileicon(type);
	}
	@SuppressLint("DefaultLocale") private String getMIMEType(File f) {
		return GetFileProperty.getfileopenkind(f);
	}
	
	
	private void setcheckboxlistener(CheckBox ck,final int position) {
		ck.setOncheckListener(new OnCheckListener() {
			
			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				if (check) {
					addlist(MainActivity.checkedfilelist, paths.get(position));
				}else {
					deletelist(MainActivity.checkedfilelist, paths.get(position));
				}
			}
		});
	}
	
	private void addlist(List<String> list,String item) {
		int x = 0;
		for (String i: list) {
			if (i.equals(item)) {
				x++;
			}
		}
		if (x == 0) {
			list.add(item);
		}
	}
	
	private void deletelist(List<String> list,String item) {
		list.remove(item);
	}
	
	private class ViewHolder {
		TextView text;
		ImageView icon;
		CheckBox checkBox;
	}
}
