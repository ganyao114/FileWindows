package com.gy.widget.meteriadialog;

import java.util.List;




import com.gy.filewindows.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	List<String> items;
	public MyListAdapter(List<String> items,Context context) {
		// TODO Auto-generated constructor stub
		this.items = items;
		inflater = LayoutInflater.from(context);
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

	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
		convertView = inflater.inflate(R.layout.listitem, null);
		holder = new ViewHolder();
		holder.textView = (TextView)convertView.findViewById(R.id.text);
		holder.imageView = (ImageView)convertView.findViewById(R.id.icon);
		convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView.setText(items.get(position));
		holder.imageView.setImageResource(geticon(items.get(position)));
		return convertView;
	}
	private class ViewHolder {
		TextView textView;
		ImageView imageView;
	}
	
	private int geticon(String str) {
		int icon=0;
		switch (str) {
		case "复制":
			icon = R.drawable.copy;
			break;
		case "粘贴":
			icon = R.drawable.paste;
			break;
		case "移动":
			icon = R.drawable.cut;
			break;
		case "重命名":
			icon = R.drawable.rename;
			break;
		case "删除":
			icon = R.drawable.delete;
			break;
		case "新建文件夹":
			icon = R.drawable.newfolder;
			break;
		case "属性":
			icon = R.drawable.ic_material_dialog_fs_warning;
			break;
		case "放置文件夹至桌面":
			icon = R.drawable.shortcut_light;
			break;
		case "将文件放置到桌面":
			icon = R.drawable.shortcut_light;
			break;
		case "加入常用文件列表":
			icon = R.drawable.bookmarck;
			break;
		default:
			break;
		}
		return icon;
	}

}
