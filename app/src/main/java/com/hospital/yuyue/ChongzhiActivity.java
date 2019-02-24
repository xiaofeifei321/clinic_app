package com.hospital.yuyue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hospital.db.HttpUtil;
import com.hospital.util.User;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class ChongzhiActivity extends Activity {
	private EditText chongzhi;
	private List<User> money = new ArrayList<User>();
	private TextView shenyu;
	private int uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chongzhi);
		chongzhi = (EditText) findViewById(R.id.chongzhi_et);
		shenyu = (TextView) findViewById(R.id.shenyu);
		Intent intent=getIntent();
		uid=intent.getIntExtra("uid", 0);
		get();
	}
	public void chongzhi_qr(View view) {
		new Thread() {
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("uid",uid);
				map.put("money",
						money.get(0).getMoney()
								+ Integer.parseInt(chongzhi.getText()
										.toString()));
				String string = HttpUtil.doPost(HttpUtil.path + "AddMoney", map);
				if (string.equals("true")) {
					handler.sendEmptyMessage(0x124);
				}
			};

		}.start();
	}

	private void get() {
		new Thread() {
			public void run() {

				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid",uid);
					String string = HttpUtil.doPost(HttpUtil.path
							+ "GetOneUser", map);
					JSONArray array = new JSONArray(string);
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						User user = new User();
						user.setMoney(object.getInt("money"));
						user.setPassword(object.getString("password"));
						user.setUid(object.getInt("uid"));
						user.setUsername(object.getString("username"));
						money.add(user);
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
				shenyu.setText(money.get(0).getMoney() + "");
			}
			if (msg.what==0x124) {
//				Toast.makeText("充值成功").show();
//				Toast.makeText(ChongzhiActivity.this, "充值成功").show();
				shenyu.setText(money.get(0).getMoney()+Integer.parseInt(chongzhi.getText()
										.toString())+"");
				finish();
			}
		};
	};
}
