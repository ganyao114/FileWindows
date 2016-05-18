package com.gy.fileserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.List;

import javax.crypto.spec.GCMParameterSpec;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.gy.filewindows.R;
import com.gy.widget.meteriadialog.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
/**
 * IO工具类 by JUST_GY
 * 文件，文件夹的复制,移动,粘贴,删除,重命名.
 * 异步多线程处理
 * 封装了ProgressDialog,显示进度条和AlertDialog,以及Notification进度条
 * 尽量使用ThreadXXX方法
 * @author gy
 *
 */
public class IoToolClass {
	private Activity activity;
	private ProgressDialog dialog, mdialog;
	private Looper looper;
	private boolean condition = true;
	private boolean deletsuccess = false;
	private AlertDialog.Builder builder;
	private Notification notification;
	private int _progress = 0;
	private static final int NOTIFICATION_ID = 0x12;
	private NotificationManager manager = null;
	private int currentprogress = 0;
	private OnDoneListener onDoneListener;
	private MaterialDialog mtdialog;
	public IoToolClass(Activity mactivity,OnDoneListener onDoneListener) {
		this.onDoneListener = onDoneListener;
		activity = mactivity;
		looper = mactivity.getMainLooper();
		dialog = new ProgressDialog(activity);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setIcon(R.drawable.app_icon);
		dialog.setTitle("处理中");
		dialog.setMax(100);
		dialog.setIndeterminate(false);
		Notification();
	}
	
	private void copyfilerow(File src, File target) {

		InputStream in = null;
		OutputStream out = null;

		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(target);
			bin = new BufferedInputStream(in);
			bout = new BufferedOutputStream(out);
			byte[] b = new byte[1024 * 10];
			int len = bin.read(b);
			while (len != -1) {
				bout.write(b, 0, len);
				len = bin.read(b);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bin != null) {
					bin.close();
				}
				if (bout != null) {
					bout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public void copyFile(File src, File target) {

		Mhandler mhandler = new Mhandler(this);
		InputStream in = null;
		OutputStream out = null;
		long count = 0;
		long filesize = getFileSizeRow(src);
		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(target);
			bin = new BufferedInputStream(in);
			bout = new BufferedOutputStream(out);
			byte[] b = new byte[1024*10];
			int len = bin.read(b);
			while (len != -1) {
				bout.write(b, 0, len);
				len = bin.read(b);
				count++;
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putSerializable("progress", (int) ((double) (1024*10*count)
						/ (double) (filesize) * 100));
				msg.setData(data);
				mhandler.sendMessage(msg);
			}
			if (condition) {
				mhandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (dialog != null) {
							dialog.dismiss();
							notificationstop();
						}
					}
				});
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bin != null) {
					bin.close();
				}
				if (bout != null) {
					bout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void moveFile(String source, String destination) {
		new File(source).renameTo(new File(destination));
	}

	public void ThreadMoveDirectory(final String srcDirName,
			final String destDirName) {
		if (new File(destDirName).exists()) {
			Toast.makeText(activity, "文件夹已存在", Toast.LENGTH_LONG).show();
		} else {
			final com.gc.materialdesign.widgets.ProgressDialog mtdialog = new  com.gc.materialdesign.widgets.ProgressDialog(activity,"处理中...");
			final Mhandler mhandler = new Mhandler(this);
			mtdialog.setCancelable(false);
			mtdialog.show();
			notificationstart();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					moveDirectory(srcDirName, destDirName);
					mhandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (mtdialog != null) {
								mtdialog.dismiss();
								notificationstop();
							}
						}
					});
					broadcastrefresh();
				}
			}).start();
		}
	}

	public boolean moveDirectory(String srcDirName, String destDirName) {

		File srcDir = new File(srcDirName);
		if (!srcDir.exists() || !srcDir.isDirectory())
			return false;

		File destDir = new File(destDirName);
		if (!destDir.exists())
			destDir.mkdirs();
		File[] sourceFiles = srcDir.listFiles();
		for (File sourceFile : sourceFiles) {
			if (sourceFile.isFile())
				moveFile(sourceFile.getPath(), destDir.getPath()
						+ File.separator + sourceFile.getName());
			else if (sourceFile.isDirectory())
				moveDirectory(sourceFile.getPath(), destDir.getPath()
						+ File.separator + sourceFile.getName());
			else
				;
		}
		return srcDir.delete();
	}

	public void ThreadCopyFile(final File src, final File target) {
		if (target.exists()) {
			Toast.makeText(activity, "文件已存在", Toast.LENGTH_LONG).show();
		} else {
			dialog.setMessage("正在复制文件" + src.getName() + " "
					+ getFileSizeMB(src));
			dialog.show();
			notificationstart();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					copyFile(src, target);
					// dialog.cancel();
					broadcastrefresh();
				}
			}).start();
		}
	}

	public void ThreadMoveFile(final String source, final String destination) {
		if (new File(destination).exists()) {
			Toast.makeText(activity, "文件已存在", Toast.LENGTH_LONG).show();
		} else {

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					moveFile(source, destination);
					broadcastrefresh();
				}
			}).start();
		}
	}

	@SuppressWarnings("resource")
	private static String getFileSizeMB(File file)

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
	/**
	 * 返回文件大小 单位B
	 * @param file
	 * @return
	 */
	@SuppressWarnings("resource")
	private static long getFileSizeRow(File file)

	{
		long size = 0;

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

		return size;

	}

	public void CopyDirectiory(String sourceDir, String targetDir) {

		(new File(targetDir)).mkdirs();

		File[] file = (new File(sourceDir)).listFiles();

		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {

				File sourceFile = file[i];

				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);

			}
			if (file[i].isDirectory()) {

				String dir1 = sourceDir + "/" + file[i].getName();

				String dir2 = targetDir + "/" + file[i].getName();
				CopyDirectiory(dir1, dir2);
			}
		}

	}

	public void ThreadCopyDirectiory(final String sourceDir,
			final String targetDir) {
		if (new File(targetDir).exists()) {
			Toast.makeText(activity, "文件夹已存在", Toast.LENGTH_LONG).show();
		} else {
			final com.gc.materialdesign.widgets.ProgressDialog mtdialog = new  com.gc.materialdesign.widgets.ProgressDialog(activity,"正在复制文件夹");
	        mtdialog.setCancelable(false);
			mtdialog.show();
			notificationstart();
			final Mhandler mhandler = new Mhandler(this);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					condition = false;
					CopyDirectiory(sourceDir, targetDir);
					condition = true;
					mhandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (mtdialog != null) {
								mtdialog.dismiss();
								notificationstop();
							}
						}
					});
					broadcastrefresh();
				}
			}).start();
		}
	}
	/**
	 * 获取文件夹下文件数量 暂时没用
	 * @param dir
	 * @return
	 */
	@SuppressWarnings("unused")
	private int getfilecount(String dir) {
		int filecount = 0;
		File file = new File(dir);
		File[] files = file.listFiles();
		if ((files != null) && (files.length > 0)) {
			for (File tmpfile : files) {

				if (tmpfile.isDirectory()) {
					getfilecount(file.getPath());
				} else {
					filecount++;
				}
			}
		}
		return filecount;

	}

	public void ThreadDeleteFile(final File file) {
		final Mhandler mhandler = new Mhandler(this);

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				deletsuccess = deleteFile(file);
				mhandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (deletsuccess) {
							Toast.makeText(activity, "删除成功", Toast.LENGTH_LONG)
									.show();
						} else {
							Toast.makeText(activity, "删除失败", Toast.LENGTH_LONG)
									.show();
						}

					}
				});
				broadcastrefresh();
			}
		}).start();
	}

	public void ThreadDeleteDirectiory(final File folder) {
		final Mhandler mhandler = new Mhandler(this);
		final com.gc.materialdesign.widgets.ProgressDialog mtdialog = new  com.gc.materialdesign.widgets.ProgressDialog(activity,"正在删除文件夹");
		mtdialog.setCancelable(false);
		mtdialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				deleteFolder(folder);
				mhandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (mtdialog != null) {
							mtdialog.dismiss();
							Toast.makeText(activity, "删除文件夹成功",
									Toast.LENGTH_LONG).show();
						}
					}
				});
				broadcastrefresh();
			}
		}).start();
	}

	public boolean deleteFolder(File folder) {
		boolean result = false;
		try {
			String childs[] = folder.list();
			if (childs == null || childs.length <= 0) {
				if (folder.delete()) {
					result = true;
				}
			} else {
				for (int i = 0; i < childs.length; i++) {
					String childName = childs[i];
					String childPath = folder.getPath() + File.separator
							+ childName;
					File filePath = new File(childPath);
					if (filePath.exists() && filePath.isFile()) {
						if (filePath.delete()) {
							result = true;
						} else {
							result = false;
							break;
						}
					} else if (filePath.exists() && filePath.isDirectory()) {
						if (deleteFolder(filePath)) {
							result = true;
						} else {
							result = false;
							break;
						}
					}
				}
				folder.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean deleteFile(File file) {
		boolean result = false;
		if (file != null) {
			try {
				File file2 = file;
				file2.delete();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}

	public void RenameFile(final String src) {
		final MaterialEditText editText = new MaterialEditText(activity);
		mtdialog = new MaterialDialog(activity);
		mtdialog.setTitle("重命名")
				.setContentView(editText)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String name = editText.getText().toString();
						if(name.equals("")){
							Toast.makeText(activity,"文件名不能为空", Toast.LENGTH_LONG).show();
						}else {
							if (new File(src).isDirectory()) {
								ThreadMoveDirectory(src, new File(src).getParent()
										+ "/" + name);
							} else {
								ThreadMoveFile(src, new File(src).getParent() + "/"
										+ name);
							}

						}
						mdialog.dismiss();
					}
				})
				.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mtdialog.dismiss();
					}
				}).show();
	}
	public void NewDirectiory(final String path)
	{
		final MaterialEditText editText = new MaterialEditText(activity);
		mtdialog = new MaterialDialog(activity);
		mtdialog.setTitle("新建文件夹")
				.setContentView(editText)
				.setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String name = editText.getText().toString();
						if (!new File(path).isDirectory()) {
							Toast.makeText(activity,"当前路径不是文件夹", Toast.LENGTH_LONG).show();
						} else {
							if (name.equals("")) {
								Toast.makeText(activity,"文件夹名字不能为空", Toast.LENGTH_LONG).show();
							}else {
								if (new File(path+"/"+name).exists()) {
									Toast.makeText(activity,"文件夹已存在", Toast.LENGTH_LONG).show();
								}else {
									boolean isok = new File(path+"/"+name).mkdir();
									if (isok) {
										broadcastrefresh();
										Toast.makeText(activity,"新建成功", Toast.LENGTH_LONG).show();
									}else {
										Toast.makeText(activity,"新建失败", Toast.LENGTH_LONG).show();
									}
								}
								
							}
						}
						mtdialog.dismiss();
					}
				})
				.setNegativeButton("取消",new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mtdialog.dismiss();
					}
				})
				.show();
	}
	
	public void multifilecopy(final List<String> items,final String targetDir) {
			final com.gc.materialdesign.widgets.ProgressDialog mtdialog = new  com.gc.materialdesign.widgets.ProgressDialog(activity,"正在复制..");
	        mtdialog.setCancelable(false);
			mtdialog.show();
			final Mhandler mhandler = new Mhandler(this);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (String i:items) {
						File tmpFile = new File(i);
						if (tmpFile.isDirectory()) {
							CopyDirectiory(i, targetDir);
						}else {
							copyfilerow(tmpFile, new File(targetDir));
						}
					}
					mhandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (mtdialog != null) {
								mtdialog.dismiss();
							}
						}
					});
					broadcastrefresh();
				}
			}).start();
		}
	
	public void mltifiledelete(final List<String> items) {
		final com.gc.materialdesign.widgets.ProgressDialog mtdialog = new  com.gc.materialdesign.widgets.ProgressDialog(activity,"正在删除..");
        mtdialog.setCancelable(false);
		mtdialog.show();
		final Mhandler mhandler = new Mhandler(this);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (String i:items) {
					File tmpFile = new File(i);
					if (tmpFile.isDirectory()) {
						deleteFolder(tmpFile);
					}else {
						deleteFile(tmpFile);
					}
				}
				mhandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (mtdialog != null) {
							mtdialog.dismiss();
						}
					}
				});
				broadcastrefresh();
			}
		}).start();
	}
	
	public void search(String dir, final String name, List<String> list) {
		File file = new File(dir);
		File[] files = file.listFiles();
		if ((files != null) && (files.length > 0)) {
			for (File tmpfile : files) {

				if (tmpfile.isDirectory()) {
					try {
						search(tmpfile.getPath(), name, list);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				} else {

					if (tmpfile.getName().indexOf(name) != -1) {
						list.add(tmpfile.getPath());
					}
				}
			}
		}

	}
	
	
	@SuppressWarnings({ "deprecation" })
	private void Notification(){
		notification = new Notification(R.drawable.app_icon, "进度条", System
                .currentTimeMillis());
        notification.icon = R.drawable.app_icon;

       
        notification.contentView = new RemoteViews(activity.getApplication()
                .getPackageName(), R.layout.notification_layout);
        notification.contentView.setProgressBar(R.id.progressBar1, 100, 0, false);
        notification.contentView.setTextViewText(R.id.textView1, "进度" + _progress
                + "%");
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        WeakReference<Activity> mactivity = new WeakReference<Activity>(activity);
        notification.contentIntent = PendingIntent.getActivity(activity, 0,
                new Intent(Intent.ACTION_MAIN).setClass(activity, activity.getClass()).addCategory(Intent.CATEGORY_LAUNCHER).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK), 0);
       
        manager = (NotificationManager)((Activity) mactivity.get()).getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		
	}
	private void notificationstart() {
		currentprogress = 0;
		manager.notify(NOTIFICATION_ID, notification);
	}
	private void notificationstop() {
		manager.cancel(NOTIFICATION_ID);
		currentprogress =0;
	}
	private void updateprogressbar(int value) {
		dialog.setProgress(value);
	}
	private void updatenotificationprogress(int value) {
		notification.contentView.setProgressBar(R.id.progressBar1, 100, value,
                false);
        notification.contentView.setTextViewText(R.id.textView1, "进度"
                + value + "%");
        
        manager.notify(NOTIFICATION_ID, notification);
	}
	
	private void broadcastrefresh() {
		onDoneListener.onDone();
	}
	private boolean updatecontral(int value) {
		boolean result =false;
		if (value%5==0&&value>currentprogress) {
			currentprogress = value;
			result = true;
		}
		return result;
	}
	private static class Mhandler extends Handler {
		private final WeakReference<IoToolClass> mActivity;

		public Mhandler(IoToolClass activity) {
			super(activity.looper);
			mActivity = new WeakReference<IoToolClass>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int progress = (int) msg.getData().getSerializable("progress");
			((IoToolClass) mActivity.get()).updateprogressbar(progress);
			if (((IoToolClass) mActivity.get()).updatecontral(progress)) {
				((IoToolClass) mActivity.get()).updatenotificationprogress(progress);
			}
		}

	}
	
}
