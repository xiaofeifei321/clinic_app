package com.hospital.yuyue;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.hospital.db.HttpUtil;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginActivity extends Activity {
	public int uid;
	public static String usernamesString;
	private EditText username, password;
	SharedPreferences spf;
	SharedPreferences spf1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		boolean boo = isFistGoing();
		if (boo) {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
		}
	}

	public void regClick(View view) {
		startActivity(new Intent(LoginActivity.this, RegActivity.class));
	}

	public void loginClick(View view) {
		new Thread() {
			public void run() {

				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("username", username.getText().toString());
					map.put("password", password.getText().toString());
					String string = HttpUtil.doPost(HttpUtil.path
							+ "LoginServlet", map);
					JSONObject object = new JSONObject(string);
					if (object.getInt("uid") > 0) {
						uid = object.getInt("uid");
						usernamesString = username.getText().toString();
						spf1 = getSharedPreferences("getuid", MODE_PRIVATE);
						Editor editor = spf1.edit();
						editor.putInt("uid", uid);
						editor.commit();
						handler.sendEmptyMessage(0x123);
					} else {
						handler.sendEmptyMessage(0x124);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			};

		}.start();
	}

	private boolean isFistGoing() {
		spf = getSharedPreferences("itcast", MODE_PRIVATE);
		boolean boo = spf.getBoolean("flag", false);
		return boo;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				Editor editor = spf.edit();
				editor.putBoolean("flag", true);
				editor.commit();
				Toast.makeText(LoginActivity.this, "µÇÂ¼³É¹¦", 1).show();
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				finish();
			}
			if (msg.what == 0x124) {
				Toast.makeText(LoginActivity.this, "µÇÂ¼Ê§°Ü", 1).show();
			}

		};
	};

}
