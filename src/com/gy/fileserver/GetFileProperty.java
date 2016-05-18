package com.gy.fileserver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.gy.filewindows.R;
import com.gy.widget.meteriadialog.MaterialDialog;

public class GetFileProperty {
	@SuppressLint("DefaultLocale") public static String getfileopenkind (File f) {
		String filetype = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			filetype = "audio/*";
		} else if (end.equals("3gp") || end.equals("mp4") || end.equals("rmvb")
				|| end.equals("rm") || end.equals("avi") || end.equals("mkv")) {
			filetype = "video/*";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			filetype = "image/*";
		} else if (end.equals("txt") || end.equals("umf") || end.equals("pdf")
				|| end.equals("doc") || end.equals("xml")) {
			filetype = "text/*";

		} else if (end.equals("apk")) {
			filetype = "application/vnd.android.package-archive";
		} else {
			filetype = "*/*";
		}

		return filetype;
	}
	
	@SuppressLint("DefaultLocale") public static String getfileopenkindbyname (String fName) {
		String filetype = "";
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			filetype = "audio/*";
		} else if (end.equals("3gp") || end.equals("mp4") || end.equals("rmvb")
				|| end.equals("rm") || end.equals("avi") || end.equals("mkv")) {
			filetype = "video/*";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			filetype = "image/*";
		} else if (end.equals("txt") || end.equals("umf") || end.equals("pdf")
				|| end.equals("doc") || end.equals("xml")) {
			filetype = "text/*";

		} else if (end.equals("apk")) {
			filetype = "application/vnd.android.package-archive";
		} else {
			filetype = "*/*";
		}

		return filetype;
	}
	
	public static int getfileicon(String type) {
		int icon = 0;
		switch (type) {
		case "audio/*":
			icon = R.drawable.ic_em_music;
			break;
		case "video/*":
			icon = R.drawable.ic_em_video;
			break;
		case "image/*":
			icon = R.drawable.ic_em_image;
			break;
		case "text/*":
			icon = R.drawable.ic_material_light_copy;
			break;
		case "application/vnd.android.package-archive":
			icon = R.drawable.ic_material_dialog_fs_warning;
			break;
		case "application/*":
			icon = R.drawable.ic_material_light_filesystem;
			break;
		default:
			icon = R.drawable.ic_em_document;
			break;
		}
		return icon;
	}
	
	@SuppressWarnings("resource")
	public static String getFileSizeMb(File file)

	{
		DecimalFormat df = new DecimalFormat("#.00");
		long size = 0;
		String sizemb;

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			size = fis.available();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (size / 1048576 > 0) {
			sizemb = df.format((double) size / 1048576) + "MB";
		} else {
			sizemb = "0" + df.format((double) size / 1048576) + "MB";
		}

		return sizemb;

	}
	public static Drawable getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false;
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;

		bitmap = BitmapFactory.decodeFile(imagePath, options);

		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		@SuppressWarnings("deprecation")
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	public static Bitmap getVideoThumbnail(String videoPath, int width, int height,
			int kind) {
		Bitmap bitmap = null;

		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		System.out.println("w" + bitmap.getWidth());
		System.out.println("h" + bitmap.getHeight());
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	
	public static void search(String dir, final String[] suffix,List<String> list) 
	{
		File file = new File(dir);
		File[] files = file.listFiles();
		if ((files != null) && (files.length > 0)) {
			for (File tmpfile : files) {

				if (tmpfile.isDirectory()) {
					search(tmpfile.getPath(), suffix, list);
				} else {

					if (isendwith(tmpfile, suffix)) {
						list.add(tmpfile.getPath());
					}
				}
			}
		}
	}
	
	@SuppressLint("ResourceAsColor") public static void getproperty(File file,Context context) {
		final MaterialDialog mtDialog = new MaterialDialog(context);
		mtDialog.setTitle("属性");
		TextView textView = new TextView(context);
		textView.setTextColor(R.color.black);
		textView.setHorizontallyScrolling(true);
		String path = file.getPath();
		if (!file.isDirectory()) {
		String filename = file.getName();
		String filekind = getfileopenkind(file);
		String filesize = getFileSizeMb(file);
		
		long filelastmodi = file.lastModified();
		textView.append("文件名:   "+filename+"\n");
		textView.append("文件类型: "+filekind+"\n");
		textView.append("大小:     "+filesize+"\n");
		textView.append("路径:     "+path+"\n");
		textView.append("修改日期: "+filelastmodi);
		}else {
			textView.append("文件夹名: "+file.getName()+"\n");
			textView.append("类型:     "+"文件夹"+"\n");
			textView.append("路径:     "+path);
		}
		mtDialog.setContentView(textView);
		mtDialog.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mtDialog.dismiss();
			}
		});
		mtDialog.show();
	}
	
	public static boolean isendwith(File f, String[] endStrings) {
		boolean tempBoolean = false;
		for (int i = 0; i < endStrings.length; i++) {
			if (f.getPath().endsWith(endStrings[i])) {
				tempBoolean = true;
			}

		}
		return tempBoolean;
	}

}
