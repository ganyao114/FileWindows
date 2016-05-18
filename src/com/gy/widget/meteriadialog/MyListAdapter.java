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
		case "����":
			icon = R.drawable.copy;
			break;
		case "ճ��":
			icon = R.drawable.paste;
			break;
		case "�ƶ�":
			icon = R.drawable.cut;
			break;
		case "������":
			icon = R.drawable.rename;
			break;
		case "ɾ��":
			icon = R.drawable.delete;
			break;
		case "�½��ļ���":
			icon = R.drawable.newfolder;
			break;
		case "����":
			icon = R.drawable.ic_material_dialog_fs_warning;
			break;
		case "�����ļ���������":
			icon = R.drawable.shortcut_light;
			break;
		case "���ļ����õ�����":
			icon = R.drawable.shortcut_light;
			break;
		case "���볣���ļ��б�":
			icon = R.drawable.bookmarck;
			break;
		default:
			break;
		}
		return icon;
	}

}
