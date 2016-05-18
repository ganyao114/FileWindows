package com.gy.widget.recyclerview;

import java.io.File;
import java.util.List;

import com.gy.fileserver.GetFileProperty;
import com.gy.filewindows.MainActivity;
import com.gy.filewindows.R;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by baoyz on 2014/6/29.
 */
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
	private MyItemClickListener itemClickListener;
	private MyItemLongClickListener itemLongClickListener;
	public  MyViewHolder myholder;
    // 数据集
    private List<String> mDataset;

    public MyAdapter(List<String> dataset) {
        super();
        mDataset = dataset;
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        View view = View.inflate(viewGroup.getContext(),R.layout.filelistlayout, null);
        // 创建一个ViewHolder
        MyViewHolder holder = new MyViewHolder(view,itemClickListener,itemLongClickListener);
        return holder;
    }
    
    public void add(String s) {
        mDataset.add(s);
        notifyItemInserted(mDataset.size()-1);
    }
    
    public void clear() {
    	mDataset.clear();
    	notifyDataSetChanged();
	}
    
    
    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        // 绑定数据到ViewHolder上
    	myholder = holder;
		if (mDataset.get(i).toString().equals("back")) {
			holder.mTextView.setText("返回...");
			holder.imageView.setImageResource(R.drawable.app_icon);
		} else {
			File f = new File(mDataset.get(i).toString());
			holder.mTextView.setText(f.getName());
			if (f.isDirectory()) {
				holder.imageView.setImageResource(R.drawable.app_icon);;
			} else {
				holder.imageView.setImageResource(iconselect(getMIMEType(f)));
			}
		}
    }
    private int iconselect(String type) {
		return GetFileProperty.getfileicon(type);
	}
	private String getMIMEType(File f) {
		return GetFileProperty.getfileopenkind(f);
	}

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    
    public void setcheckboxvisible() {
		MainActivity.isshowck = true;
		notifyDataSetChanged();
	}
    
    public void setOnItemClickListener(MyItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}
   
    public void setOnItemLongClickListner(MyItemLongClickListener itemLongClickListener) {
		this.itemLongClickListener = itemLongClickListener;
	}
}
