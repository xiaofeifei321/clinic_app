package com.hospital.fragment;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hospital.db.HttpUtil;
import com.hospital.util.Events;
import com.hospital.util.Ques;
import com.hospital.util.SystemHelper;
import com.hospital.yuyue.Content_Activity;
import com.hospital.yuyue.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class QuesActivity extends Activity {
	private View view;
	private TextView tView1, tView2,tView3, tView4,key_;
	private LinearLayout layout1, layout2,layout3, layout4;
	
	private List<Ques> list = new ArrayList<Ques>();
	private ListView listView;
	private myadapter adapter;
	private String key;
	
	private SharedPreferences spf;
	private int uid;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ques);
		key_ = (TextView) findViewById(R.id.key_);
		listView = (ListView) findViewById(R.id.homelist);
		spf = getSharedPreferences("getuid", 0);
		uid = spf.getInt("uid", 0);
	/*	listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String name = list.get(arg2).mname;
				Intent intent = new Intent(QuesActivity.this,
						Content_Activity.class);
				intent.putExtra("mname", name);
				startActivity(intent);
			}
		});*/
	/*	key = getIntent().getExtras().getString("key");
		key_.setText(key);*/
		getAllTwo();
	}
    
    
	public void getAllTwo() {
		list.removeAll(list);
		new Thread() {
			public void run() {
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", uid);
					String str = HttpUtil.doPost(HttpUtil.pathstr
							+ "GetQuesServlet", map);
					JSONArray jsonArray = new JSONArray(str);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Ques bk = new Ques();
						bk.setLid(jsonObject.getInt("lid"));
						bk.setUid(jsonObject.getInt("uid"));
						bk.setContent(jsonObject.getString("content"));
						bk.setReply(jsonObject.getString("reply"));
						bk.setName(jsonObject.getString("name"));
						list.add(bk);
					}
					handler.sendEmptyMessage(0x222);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private class myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View v, ViewGroup arg2) {
			// TODO Auto-generated method stub
			v = LayoutInflater.from(QuesActivity.this).inflate(
					R.layout.ques_list_item, null);
			ImageView picImageView = (ImageView) v.findViewById(R.id.list_mpic);
		/*	new myasy(picImageView).execute(HttpUtil.picstr
					+ list.get(arg0).getMpic());*/
			TextView mname = (TextView) v.findViewById(R.id.list_mname);
			TextView mcontent = (TextView) v.findViewById(R.id.list_mcontent);
			TextView mtype = (TextView) v.findViewById(R.id.list_mtype);
			TextView username = (TextView) v.findViewById(R.id.list_username);
			TextView mtime = (TextView) v.findViewById(R.id.list_mtime);
			mname.setText(list.get(arg0).getName());
//			username.setText(list.get(arg0).getUsername());
//			mtime.setText(list.get(arg0).getMtime());
			mcontent.setText(list.get(arg0).getReply());
			mtype.setText(list.get(arg0).getContent());
			return v;
		}
	}

	private class myasy extends AsyncTask<String, Void, Bitmap> {
		ImageView ivg;

		public myasy(ImageView ivg) {
			// TODO Auto-generated constructor stub
			this.ivg = ivg;
		}

		@Override
		protected Bitmap doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			Bitmap bitmap = null;
			try {
				URL url = new URL(arg0[0]);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				InputStream isInputStream = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(isInputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ivg.setImageBitmap(result);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x222) {
				adapter = new myadapter();
				listView.setAdapter(adapter);
			}
		};
	};

}
