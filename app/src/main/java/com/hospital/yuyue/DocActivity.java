package com.hospital.yuyue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hospital.db.HttpUtil;
import com.hospital.util.Office;

import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class DocActivity extends Activity {
	private TextView content, docname, oname, phone, need,ontime;
	private int oid;
	private List<Office> list = new ArrayList<Office>();
	private SharedPreferences spf;
	private int uid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_doc);
		content = (TextView) findViewById(R.id.doc_content);
		docname = (TextView) findViewById(R.id.doc_name);
		oname = (TextView) findViewById(R.id.doc_oname);
		phone = (TextView) findViewById(R.id.doc_phone);
		need = (TextView) findViewById(R.id.doc_need);
		ontime = (TextView) findViewById(R.id.ontime);
		Intent intent = getIntent();
		oid = intent.getIntExtra("oid", 0);
		spf = getSharedPreferences("getuid", 0);
		uid = spf.getInt("uid", 0);
		// 设置textview 滚动
		content.setMovementMethod(ScrollingMovementMethod.getInstance());
		get();
	}

	private void get() {
		new Thread() {
			public void run() {
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("oid", oid);
					String string = HttpUtil.doPost(HttpUtil.path
							+ "GetOneOffice", map);
					JSONArray array = new JSONArray(string);
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						Office office = new Office();
						office.setContent(object.getString("content"));
						office.setDocname(object.getString("docname"));
						office.setNeed(object.getInt("need"));
						office.setOid(object.getInt("oid"));
						office.setOname(object.getString("oname"));
						office.setPhone(object.getString("phone"));
						office.setOntime(object.getString("ontime"));
						list.add(office);
					}
					handler.sendEmptyMessage(0x123);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			};

		}.start();

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				content.setText(list.get(0).getContent());
				oname.setText(list.get(0).getOname());
				docname.setText(list.get(0).getDocname());
				need.setText(list.get(0).getNeed() + "");
				phone.setText(list.get(0).getPhone());
				ontime.setText(list.get(0).getOntime());
			}
			if (msg.what==0x124) {
				Toast.makeText(DocActivity.this, "预约成功",1).show();
				finish();
			}
			if (msg.what==0x129) {
				Toast.makeText(DocActivity.this, "预约已满",1).show();
				finish();
			}
			if (msg.what==0x125) {
				Toast.makeText(DocActivity.this, "投诉成功，谢谢您的反馈",1).show();
				finish();
			}
		};
	};

	public void qeurenyy(View view) {
		new Thread(){
			public void run() {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("uid", uid);
				map.put("oid",oid);
				String string=HttpUtil.doPost(HttpUtil.path+"AddSub", map);
				if (string.equals("true")) {
					handler.sendEmptyMessage(0x124);
				}else if (string.equals("false1")) {
					handler.sendEmptyMessage(0x129);
				}
				
			};
			
		}.start();

	}
	public void tousu(View view) {
		new Thread(){
			public void run() {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("username", LoginActivity.usernamesString);
				map.put("doc_name",docname.getText().toString());
				map.put("doc_office",oname.getText().toString());
				String string=HttpUtil.doPost(HttpUtil.path+"AddTousu", map);
				if (string.equals("true")) {
					handler.sendEmptyMessage(0x125);
				}
				
			};
			
		}.start();
		
	}
	public void zixun(View view) {
		Intent intent =new Intent(DocActivity.this,AddQues.class);
		intent.putExtra("uid", uid);
		startActivity(intent);
		
	}

}
