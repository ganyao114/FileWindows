package com.gy.filewindows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.widgets.ProgressDialog;
import com.gy.fileserver.DataBaseHelper;
import com.gy.fileserver.GetFileProperty;
import com.gy.fileserver.IoToolClass;
import com.gy.fileserver.OnDoneListener;
import com.gy.fileserver.Openfileshortcut;
import com.gy.filewindows.R;
import com.gy.filewindows.appwidget.listwidgetprovider;
import com.gy.filewindows.downloader.DownloadDialog;
import com.gy.filewindows.setting.Setting;
import com.gy.widget.floatactionmenu.FloatingActionButton;
import com.gy.widget.floatactionmenu.FloatingActionsMenu;
import com.gy.widget.listview.Filelistadapter;
import com.gy.widget.meteriadialog.MaterialDialog;
import com.gy.widget.meteriadialog.MyListAdapter;
import com.gy.widget.recyclerview.MyAdapter;
import com.gy.widget.recyclerview.MyItemClickListener;
import com.gy.widget.recyclerview.MyItemLongClickListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks, MyItemClickListener, MyItemLongClickListener {

	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	MyAdapter myadapter = null;
	private String rootpath = "/";
	private List<String> item, paths;
	private String pathnow = "/";
	private String src = null;
	private String srcfilename = null;
	private RecyclerView myrecyclerView;
	private MaterialDialog dialog;
	private boolean iscurrenthavepastefile = false;
	private boolean iscut = false;
	private IoToolClass iotoolclass;
	private CheckBox ck;
	private FloatingActionsMenu floatactionmenu;
	private FloatingActionButton actionbutton1, actionbutton2, actionbutton3,
			actionbutton4, actionbutton5,actionbutton7;
	public static boolean isshowck = false;
	private boolean islistview = true;
	private ListView listview;
	public static List<String> checkedfilelist;
	private LayoutAnimationController animationController;
	private TextView curruntpathtv;
	private long exittime = 0;
	public static boolean isfirstrun = false;
	private boolean isfirstclick = true;
	private String currentdatabase;
	private String[] currentsuffix;
	private List<String> multifilelist;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_topdrawer);
		if (DataBaseHelper.isfirstrun(this)) {
			List<String> databasenames = new ArrayList<String>();
			databasenames.add("textdatabase");
			databasenames.add("apkdatabase");
			databasenames.add("videodatabase");
			databasenames.add("compassdatabase");
			databasenames.add("photodatabase");
			databasenames.add("musicdatabase");
			databasenames.add("bookmarkdatabase");
			DataBaseHelper.initdatabase(databasenames, this);
			firstruntip();
		}
		curruntpathtv = (TextView) findViewById(R.id.textView1);
		animationController = new LayoutAnimationController(
				AnimationUtils.loadAnimation(MainActivity.this,
						R.anim.item_animation));
		animationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
		animationController.setDelay((float) 0.15);
		checkedfilelist = new ArrayList<String>();
		listview = (ListView) findViewById(R.id.listview);
		actionbutton1 = (FloatingActionButton) findViewById(R.id.action_a);
		actionbutton2 = (FloatingActionButton) findViewById(R.id.action_b);
		actionbutton3 = (FloatingActionButton) findViewById(R.id.action_c);
		actionbutton4 = (FloatingActionButton) findViewById(R.id.action_d);
		actionbutton5 = (FloatingActionButton) findViewById(R.id.action_e);
		actionbutton7 = (FloatingActionButton) findViewById(R.id.action_g);
		ck = (CheckBox) findViewById(R.id.checkBox1);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		if (islistview) {
			if (getIntent().getStringExtra("path") != null) {
				getdirlistview(getIntent().getStringExtra("path"));
			} else {
				getdirlistview(rootpath);
			}

		} else {
			initVertical();
		}
		iotoolclass = new IoToolClass(this, new OnDoneListener() {

			@Override
			public void onDone() {
				// TODO Auto-generated method stub
				refreshlist();
			}
		});
		actionbutton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (islistview) {
					if (isshowck) {
						isshowck = false;
					} else {
						isshowck = true;
					}
					refreshlist();
				} else {
					myadapter.setcheckboxvisible();
				}

			}
		});
		actionbutton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (String i : checkedfilelist) {
					Log.e("gy", i);
				}
			}
		});
		actionbutton3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				multifilesexec(null, "copy", pathnow);
			}
		});
		actionbutton4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				multifilesexec(null, "paste", pathnow);
			}
		});
		actionbutton5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				multifilesexec(null, "delete", pathnow);
			}
		});
		actionbutton7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refreshkindlist(currentdatabase, "filelist", currentsuffix,
						paths);
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				File templefile = new File(paths.get(position));
				if (templefile.isDirectory()) {
					try {
						getdirlistview(paths.get(position));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						getdirlistview(templefile.getParent());
						Toast.makeText(getApplicationContext(), "未获得权限无法查看",
								Toast.LENGTH_LONG).show();
					}
				} else {
					try {
						openFile(templefile);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(MainActivity.this, "打开失败",
								Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
			}
		});
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final File file = new File(paths.get(position));
				List<String> array = new ArrayList<String>();
				if (file.isDirectory()) {
					array.add("复制");
					array.add("移动");
					array.add("重命名");
					array.add("删除");
					array.add("新建文件夹");
					array.add("放置文件夹至桌面");
					array.add("属性");
					if (iscurrenthavepastefile) {
						array.add("粘贴");
					}
				} else {

					array.add("将文件放置到桌面");
					array.add("加入常用文件列表");
					array.add("复制");
					array.add("移动");
					array.add("重命名");
					array.add("删除");
					array.add("新建文件夹");
					array.add("属性");
					if (iscurrenthavepastefile) {
						array.add("粘贴");
					}
				}
				ListView listView = new ListView(MainActivity.this);
				listView.setLayoutParams(new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
				// float scale = getResources().getDisplayMetrics().density;
				// int dpAsPixels = (int) (8 * scale + 0.5f);
				// listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
				// listView.setDividerHeight(0);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (position == 0 && !file.isDirectory()) {
							Intent shortcutIntent = new Intent();
							Intent openfileIntent = new Intent(
									MainActivity.this, Openfileshortcut.class);
							openfileIntent.putExtra("filepath", file.getPath());
							shortcutIntent
									.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
							shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
									file.getName());
							shortcutIntent
									.putExtra(
											Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
											Intent.ShortcutIconResource
													.fromContext(
															MainActivity.this,
															GetFileProperty
																	.getfileicon(getMIMEType(file))));
							shortcutIntent.putExtra(
									Intent.EXTRA_SHORTCUT_INTENT,
									openfileIntent);
							sendBroadcast(shortcutIntent);
						} else if (position == 1 && !file.isDirectory()) {

							
							DataBaseHelper dHelper = new DataBaseHelper(
									getApplicationContext(),
									"bookmarkdatabase", null, 1);
							final SQLiteDatabase db = dHelper
									.getWritableDatabase();
							ContentValues contentValues = new ContentValues();
							contentValues.put("path", file.getPath());
							db.insert("filelist", "_id", contentValues);
							db.close();
							dHelper.close();

						} else if (position == 2 && !file.isDirectory()) {
							iscurrenthavepastefile = true;
							iscut = false;
							src = file.getPath();
							srcfilename = file.getName();
						} else if (position == 3 && !file.isDirectory()) {
							iscurrenthavepastefile = true;
							iscut = true;
							src = file.getPath();
							srcfilename = file.getName();
						} else if (position == 8 && !file.isDirectory()) {
							iscurrenthavepastefile = false;
							if (iscut) {
								if (new File(src).isDirectory()) {
									iotoolclass.ThreadMoveDirectory(src,
											file.getParent() + "/"
													+ srcfilename);
								} else {
									iotoolclass.ThreadMoveFile(src,
											file.getParent() + "/"
													+ srcfilename);
								}

							} else {
								if (new File(src).isDirectory()) {
									iotoolclass.ThreadCopyDirectiory(src,
											file.getParent() + "/"
													+ srcfilename);
								} else {
									iotoolclass.ThreadCopyFile(new File(src),
											new File(file.getParent() + "/"
													+ srcfilename));
								}

							}

						} else if (position == 7 && file.isDirectory()) {
							iscurrenthavepastefile = false;
							if (iscut) {
								if (new File(src).isDirectory()) {
									iotoolclass.ThreadMoveDirectory(src,
											file.getPath() + "/" + srcfilename);
								} else {
									iotoolclass.ThreadMoveFile(src,
											file.getPath() + "/" + srcfilename);
								}

							} else {
								if (new File(src).isDirectory()) {
									iotoolclass.ThreadCopyDirectiory(src,
											file.getPath() + "/" + srcfilename);
								} else {
									iotoolclass.ThreadCopyFile(new File(src),
											new File(file.getPath() + "/"
													+ srcfilename));
								}

							}

						} else if (position == 0 && file.isDirectory()) {
							iscurrenthavepastefile = true;
							iscut = false;
							src = file.getPath();
							srcfilename = file.getName();
						} else if (position == 1 && file.isDirectory()) {
							iscurrenthavepastefile = true;
							iscut = true;
							src = file.getPath();
							srcfilename = file.getName();
						} else if (position == 3 && file.isDirectory()) {
							iotoolclass.ThreadDeleteDirectiory(file);

						} else if (position == 5 && !file.isDirectory()) {
							iotoolclass.ThreadDeleteFile(file);

						} else if (position == 2 && file.isDirectory()) {
							iotoolclass.RenameFile(file.getPath());

						} else if (position == 4 && !file.isDirectory()) {
							iotoolclass.RenameFile(file.getPath());

						} else if (position == 4 && file.isDirectory()) {
							iotoolclass.NewDirectiory(file.getPath());
						} else if (position == 6 && !file.isDirectory()) {
							iotoolclass.NewDirectiory(file.getParent());
						} else if (position == 5 && file.isDirectory()) {
							Intent shortcutIntent = new Intent();
							Intent openfileIntent = new Intent(
									MainActivity.this, Openfileshortcut.class);
							openfileIntent.putExtra("filepath", file.getPath());
							shortcutIntent
									.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
							shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
									file.getName());
							shortcutIntent.putExtra(
									Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
									Intent.ShortcutIconResource.fromContext(
											MainActivity.this,
											R.drawable.app_icon));
							shortcutIntent.putExtra(
									Intent.EXTRA_SHORTCUT_INTENT,
									openfileIntent);
							sendBroadcast(shortcutIntent);
						}else if(position == 7 && !file.isDirectory()) {
							GetFileProperty.getproperty(file, MainActivity.this);
						}else if(position == 6 && file.isDirectory()){
							GetFileProperty.getproperty(file, MainActivity.this);
						}
						dialog.dismiss();
					}
				});
				listView.setAdapter(new MyListAdapter(array, MainActivity.this));
				dialog = new MaterialDialog(MainActivity.this);
				dialog.setContentView(listView);
				dialog.setTitle("选项");
				dialog.setPositiveButton("OK", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.show();
				return true;
			}
		});
		listview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case KeyEvent.ACTION_DOWN:
					curruntpathtv.setVisibility(View.VISIBLE);
					curruntpathtv.setAnimation(AnimationUtils.loadAnimation(
							MainActivity.this, R.anim.tv_animation));
					break;
				case KeyEvent.ACTION_UP:
					curruntpathtv.setAnimation(AnimationUtils.loadAnimation(
							MainActivity.this, R.anim.tvgone_animation));
					curruntpathtv.setVisibility(View.GONE);
				default:
					break;
				}
				return false;
			}
		});
	}

	public void initVertical() {
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_vertical);

		// 创建一个线性布局管理器
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		// 默认是Vertical，可以不写
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		// 设置布局管理器
		recyclerView.setLayoutManager(layoutManager);

		// 创建数据集
		getdir(rootpath);
		// 创建Adapter，并指定数据集
		MyAdapter adapter = new MyAdapter(item);
		adapter.setOnItemClickListener(this);
		adapter.setOnItemLongClickListner(this);
		myadapter = adapter;

		// 设置Adapter
		recyclerView.setAdapter(adapter);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		myrecyclerView = recyclerView;
	}

	@SuppressLint("DefaultLocale")
	private String getMIMEType(File f) {
		return GetFileProperty.getfileopenkind(f);
	}

	private void openFile(File f) throws Exception {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		startActivity(intent);
	}

	private void getdirlistview(String filepath) {
		pathnow = filepath;
		curruntpathtv.setText(filepath);
		item = new ArrayList<String>();
		paths = new ArrayList<String>();
		File file = new File(filepath);
		File[] files = file.listFiles();
		if (!filepath.equals(rootpath)) {
			item.add("back");
			paths.add(file.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File tempfile = files[i];
			item.add(tempfile.getName());
			paths.add(tempfile.getPath());
		}
		listview.setAdapter(new Filelistadapter(getApplicationContext(), item,
				paths));
		listview.setLayoutAnimation(animationController);
	}

	@SuppressWarnings("unused")
	private void getdir(String filepath) {
		pathnow = filepath;
		if (paths == null) {
			paths = new ArrayList<String>();
		} else {
			paths.clear();
		}
		if (item == null) {
			item = new ArrayList<String>();
		} else {
			item.clear();
		}
		File file = new File(filepath);
		File[] files = file.listFiles();
		if (!filepath.equals(rootpath)) {
			item.add("back");
			paths.add(file.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File tempfile = files[i];
			item.add(tempfile.getPath());
			paths.add(tempfile.getPath());
		}
		if (myadapter != null) {
			myadapter.notifyDataSetChanged();
		}
	}

	@SuppressWarnings("unused")
	private void getdirwithanimator(String filepath) {
		pathnow = filepath;
		File file = new File(filepath);
		File[] files = file.listFiles();
		myadapter.clear();
		if (!filepath.equals(rootpath)) {
			myadapter.add("back");
			paths.add(file.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File tempfile = files[i];
			myadapter.add(tempfile.getPath());
			paths.add(tempfile.getPath());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		isshowck = false;
		if (position != 0) {
			actionbutton1.setVisibility(View.GONE);
			actionbutton2.setVisibility(View.GONE);
			actionbutton3.setVisibility(View.GONE);
			actionbutton4.setVisibility(View.GONE);
			actionbutton7.setVisibility(View.VISIBLE);
		} else if (!isfirstclick) {
			actionbutton1.setVisibility(View.VISIBLE);
			actionbutton2.setVisibility(View.VISIBLE);
			actionbutton3.setVisibility(View.VISIBLE);
			actionbutton4.setVisibility(View.GONE);
			actionbutton7.setVisibility(View.GONE);
		}
		switch (position) {
		case 0:
			if (isfirstclick) {
				isfirstclick = false;
			} else {
				getdirlistview(rootpath);
			}

			break;
		case 1:
			kindpage("textdatabase", new String[] { "txt", "doc", "pdf", "umd",
					"ppt" });
			break;
		case 2:
			kindpage("apkdatabase", new String[] { "apk" });
			break;
		case 3:
			kindpage("videodatabase", new String[] { "rmvb", "avi", "3gp",
					"mp4", "mpeg", "rm", "mkv" });
			break;
		case 4:
			kindpage("compassdatabase", new String[] { "rar", "zip", "7z",
					"gz", "tar" });
			break;
		case 5:
			kindpage("photodatabase", new String[] { "png", "gif", "bmp",
					"jpg", "jpeg", "jpe" });
			break;
		case 6:
			kindpage("musicdatabase", new String[] { "mp3", "wav", "wma",
					"flac", "ape", "ogg" });
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (mNavigationDrawerFragment.isDrawerOpen())
			mNavigationDrawerFragment.closeDrawer();
		else
			super.onBackPressed();
	}

	@Override
	public void onItemClick(View view, int position) {
		// TODO Auto-generated method stub
		File templefile = new File(paths.get(position));
		if (templefile.isDirectory()) {
			try {
				getdir(paths.get(position));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				getdir(templefile.getParent());
				Toast.makeText(getApplicationContext(), "未获得权限无法查看",
						Toast.LENGTH_LONG).show();
			}
		} else {
			try {
				openFile(templefile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(MainActivity.this, "打开失败", Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onLongItemClick(View view, int position) {
		// TODO Auto-generated method stub
		final File file = new File(paths.get(position));
		List<String> array = new ArrayList<String>();
		if (file.isDirectory()) {
			array.add("复制");
			array.add("移动");
			array.add("重命名");
			array.add("删除");
			array.add("新建文件夹");
			array.add("放置文件夹至桌面");
			array.add("属性");
			if (iscurrenthavepastefile) {
				array.add("粘贴");
			}
		} else {

			array.add("将文件放置到桌面");
			array.add("加入常用文件列表");
			array.add("复制");
			array.add("移动");
			array.add("重命名");
			array.add("删除");
			array.add("新建文件夹");
			if (iscurrenthavepastefile) {
				array.add("粘贴");
			}
		}
		ListView listView = new ListView(this);
		listView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		// float scale = getResources().getDisplayMetrics().density;
		// int dpAsPixels = (int) (8 * scale + 0.5f);
		// listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
		// listView.setDividerHeight(0);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0 && !file.isDirectory()) {
					Intent shortcutIntent = new Intent();
					Intent openfileIntent = new Intent(MainActivity.this,
							Openfileshortcut.class);
					openfileIntent.putExtra("filepath", paths.get(position));
					shortcutIntent
							.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
					shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
							file.getName());
					shortcutIntent.putExtra(
							Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
							Intent.ShortcutIconResource.fromContext(
									MainActivity.this, GetFileProperty
											.getfileicon(getMIMEType(file))));
					shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
							openfileIntent);
					sendBroadcast(shortcutIntent);
				} else if (position == 1 && !file.isDirectory()) {

					/*
					 * DataBaseHelper dHelper = new
					 * DataBaseHelper(getApplicationContext(),
					 * "lovefiledatabase", null, 1); final SQLiteDatabase db =
					 * dHelper.getWritableDatabase(); if (dHelper.isfirstcreated
					 * == true) { db.execSQL(
					 * "create table filelist(_id INTEGER PRIMARY KEY AUTOINCREMENT, path TEXT NOT NULL)"
					 * ); } ContentValues contentValues = new ContentValues();
					 * contentValues.put("path", file.getPath());
					 * db.insert("filelist", "_id", contentValues); db.close();
					 * dHelper.close();
					 */

				} else if (position == 2 && !file.isDirectory()) {
					iscurrenthavepastefile = true;
					iscut = false;
					src = file.getPath();
					srcfilename = file.getName();
				} else if (position == 3 && !file.isDirectory()) {
					iscurrenthavepastefile = true;
					iscut = true;
					src = file.getPath();
					srcfilename = file.getName();
				} else if (position == 7 && !file.isDirectory()) {
					iscurrenthavepastefile = false;
					if (iscut) {
						if (new File(src).isDirectory()) {
							iotoolclass.ThreadMoveDirectory(src,
									file.getParent() + "/" + srcfilename);
						} else {
							iotoolclass.ThreadMoveFile(src, file.getParent()
									+ "/" + srcfilename);
						}

					} else {
						if (new File(src).isDirectory()) {
							iotoolclass.ThreadCopyDirectiory(src,
									file.getParent() + "/" + srcfilename);
						} else {
							iotoolclass.ThreadCopyFile(new File(src), new File(
									file.getParent() + "/" + srcfilename));
						}

					}

				} else if (position == 6 && file.isDirectory()) {
					iscurrenthavepastefile = false;
					if (iscut) {
						if (new File(src).isDirectory()) {
							iotoolclass.ThreadMoveDirectory(src, file.getPath()
									+ "/" + srcfilename);
						} else {
							iotoolclass.ThreadMoveFile(src, file.getPath()
									+ "/" + srcfilename);
						}

					} else {
						if (new File(src).isDirectory()) {
							iotoolclass.ThreadCopyDirectiory(src,
									file.getPath() + "/" + srcfilename);
						} else {
							iotoolclass.ThreadCopyFile(new File(src), new File(
									file.getPath() + "/" + srcfilename));
						}

					}

				} else if (position == 0 && file.isDirectory()) {
					iscurrenthavepastefile = true;
					iscut = false;
					src = file.getPath();
					srcfilename = file.getName();
				} else if (position == 1 && file.isDirectory()) {
					iscurrenthavepastefile = true;
					iscut = true;
					src = file.getPath();
					srcfilename = file.getName();
				} else if (position == 3 && file.isDirectory()) {
					iotoolclass.ThreadDeleteDirectiory(file);

				} else if (position == 5 && !file.isDirectory()) {
					iotoolclass.ThreadDeleteFile(file);

				} else if (position == 2 && file.isDirectory()) {
					iotoolclass.RenameFile(file.getPath());

				} else if (position == 4 && !file.isDirectory()) {
					iotoolclass.RenameFile(file.getPath());

				} else if (position == 4 && file.isDirectory()) {
					iotoolclass.NewDirectiory(file.getPath());
				} else if (position == 6 && !file.isDirectory()) {
					iotoolclass.NewDirectiory(file.getParent());
				} else if (position == 5 && file.isDirectory()) {
					Intent shortcutIntent = new Intent();
					Intent openfileIntent = new Intent(MainActivity.this,
							Openfileshortcut.class);
					openfileIntent.putExtra("filepath", paths.get(position));
					shortcutIntent
							.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
					shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
							file.getName());
					shortcutIntent.putExtra(
							Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
							Intent.ShortcutIconResource.fromContext(
									MainActivity.this, R.drawable.app_icon));
					shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
							openfileIntent);
					sendBroadcast(shortcutIntent);
				}
				dialog.dismiss();
			}
		});
		listView.setAdapter(new MyListAdapter(array, MainActivity.this));
		dialog = new MaterialDialog(this);
		dialog.setContentView(listView);
		dialog.setTitle("选项");
		dialog.setPositiveButton("OK", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@SuppressLint("ResourceAsColor")
	private void kindpage(final String databasename, final String[] kinds) {
		currentdatabase = databasename;
		currentsuffix = kinds;
		if (DataBaseHelper.istableempty(databasename, "filelist",
				MainActivity.this)) {
			TextView tvTextView = new TextView(MainActivity.this);
			tvTextView.setText("列表空，是否刷新？");
			tvTextView.setTextColor(R.color.black);
			dialog = new MaterialDialog(MainActivity.this);
			dialog.setTitle("提示").setContentView(tvTextView)
					.setPositiveButton("是", new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							refreshkindlist(databasename, "filelist", kinds,
									paths);
						}
					}).setNegativeButton("否", new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							getdirlistview(rootpath);
							dialog.dismiss();
						}
					}).show();
		} else {
			List<String> list = DataBaseHelper.gettableitems(databasename,
					"filelist", MainActivity.this);
			boolean ischanged = false;
			for (int i = 0;i<list.size();i++) {
				if (!isfilcunzai(list.get(i))) {
					list.remove(i);
					ischanged = true;
				}
			}
			if (ischanged) {
				Toast.makeText(MainActivity.this, "文件发生变化请立即刷新",
						Toast.LENGTH_LONG).show();
			}
			listview.setAdapter(new Filelistadapter(MainActivity.this, list,
					list));
			paths = list;
		}
	}

	public void refreshlist() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (islistview) {
					getdirlistview(pathnow);
				} else {
					getdir(pathnow);
				}

			}
		});
	}

	private void multifilesexec(List<String> items, String exec, String target) {
			switch (exec) {
			case "paste":
				if (multifilelist.size() != 0) {
					iotoolclass.multifilecopy(checkedfilelist, target);
					checkedfilelist = null;
				}else {
					Toast.makeText(MainActivity.this, "未选中文件", Toast.LENGTH_SHORT).show();
				}
				break;
			case "delete":
				if (checkedfilelist.size() != 0) {
					try {
						iotoolclass.mltifiledelete(checkedfilelist);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}else {
					Toast.makeText(MainActivity.this, "未选中文件", Toast.LENGTH_SHORT).show();
				}
				break;
			case "copy":
				if (checkedfilelist.size() != 0) {
					multifilelist = checkedfilelist;
					Toast.makeText(MainActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(MainActivity.this, "未选中文件", Toast.LENGTH_SHORT).show();
				}
				
				break;
			default:
				break;
			}

		
	}

	private void refreshkindlist(final String databasename,
			final String tablename, final String[] suffix,
			final List<String> list) {

		list.clear();
		final ProgressDialog progressdialog = new ProgressDialog(
				MainActivity.this, "请稍后....");
		//progressdialog.setTitle("提示");
		//progressdialog.setMessage("请稍后...");
		progressdialog.setCancelable(false);
		progressdialog.show();
		new Thread(new Runnable() {

			@SuppressLint("SdCardPath")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetFileProperty.search("/sdcard/", suffix, list);
				DataBaseHelper.refreshtableitems(databasename, tablename, list,
						MainActivity.this);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						listview.setAdapter(new Filelistadapter(
								MainActivity.this, list, list));
						progressdialog.dismiss();
					}
				});
			}
		}).start();
	}
	 
	private void searchfile(final String path) {
		final MaterialDialog materialDialog = new MaterialDialog(MainActivity.this);
		materialDialog.setTitle("输入文件名");
		final MaterialEditText editText = new MaterialEditText(
				MainActivity.this);
		materialDialog.setContentView(editText);
		materialDialog.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this, "正在搜索...");
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
				final String filename = editText.getText().toString();
				if (filename == null) {
					Toast.makeText(MainActivity.this, "请输入文件名",
							Toast.LENGTH_LONG).show();
				} else {

					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							paths.clear();
							iotoolclass.search(path, filename, paths);
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (paths.size() == 0) {
										Toast.makeText(MainActivity.this, "未找到该文件",
												Toast.LENGTH_LONG).show();
										getdirlistview(pathnow);
									} else {
										listview.setAdapter(new Filelistadapter(MainActivity.this, paths, paths));
										listview.setLayoutAnimation(animationController);
									}
									mProgressDialog.dismiss();
								}
							});
							
						}
					}).start();
				}
				materialDialog.dismiss();
			}
		});
		materialDialog.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				materialDialog.dismiss();
			}
		});
		materialDialog.show();
	}
	
	@SuppressLint("ResourceAsColor") private void firstruntip() {
		final MaterialDialog mtdialog = new MaterialDialog(MainActivity.this);
		mtdialog.setTitle("第一次使用");
		TextView textView = new TextView(MainActivity.this);
		textView.setText("本作品为学生个人作品，有些bug和瑕疵请见谅\n"
				+ "重要提示:使用悬浮窗和桌面控件前先刷新分类界面");
		textView.setTextColor(R.color.black);
		mtdialog.setContentView(textView);
		mtdialog.setPositiveButton("我明白了", new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mtdialog.dismiss();
			}
		});
		mtdialog.show();
	}
	
	private boolean isfilcunzai(String path) {
		File file = new File(path);

		return file.exists();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		if (getIntent().getStringExtra("path") != null) {
			getdir(getIntent().getStringExtra("path"));
		}
		super.onStart();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("path", pathnow);
		setIntent(intent);
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			 if((System.currentTimeMillis() - exittime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出",
						Toast.LENGTH_LONG).show();
				exittime = System.currentTimeMillis();

			} else {
				finish();
				System.exit(0);
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_search) {
			searchfile(pathnow);
			return true;
		}else if(id == R.id.menu_setting){
			new Setting(MainActivity.this);
			return true;
		}else if (id == R.id.menu_download) {
			new DownloadDialog(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
