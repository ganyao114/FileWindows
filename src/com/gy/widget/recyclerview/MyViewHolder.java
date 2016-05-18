package com.gy.widget.recyclerview;


import com.gy.filewindows.MainActivity;
import com.gy.filewindows.R;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends ViewHolder implements OnClickListener,OnLongClickListener{
	public TextView mTextView;
	public ImageView imageView;
	private MyItemClickListener itemClickListener;
	private MyItemLongClickListener itemLongClickListener;
	public MyViewHolder(View rootView,MyItemClickListener itemClickListener,MyItemLongClickListener itemLongClickListener)
	{
		super(rootView);
        mTextView = (TextView)rootView.findViewById(R.id.text);
        imageView = (ImageView)rootView.findViewById(R.id.icon);
        if (MainActivity.isshowck) {
        	rootView.findViewById(R.id.checkBox1).setVisibility(View.VISIBLE);
		}
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        rootView.setOnClickListener(this);
        rootView.setOnLongClickListener(this);
        //rootView.setAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.anim.item_animation));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		itemClickListener.onItemClick(v, getPosition());
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		itemLongClickListener.onLongItemClick(v, getPosition());
		return false;
	}
	

}
