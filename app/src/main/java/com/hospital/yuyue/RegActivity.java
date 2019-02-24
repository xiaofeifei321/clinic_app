package com.hospital.yuyue;

import java.util.HashMap;
import java.util.Map;

import com.hospital.db.HttpUtil;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class RegActivity extends Activity {
	private EditText username, passowrd, sex, name, phone,reg_iden;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reg);
		username = (EditText) findViewById(R.id.reg_username);
		passowrd = (EditText) findViewById(R.id.reg_password);
		sex = (EditText) findViewById(R.id.reg_sex);
		name = (EditText) findViewById(R.id.reg_name);
		phone = (EditText) findViewById(R.id.reg_phone);
		reg_iden = (EditText) findViewById(R.id.reg_iden);

	}

	public void RegBtnClick(View view) {
		new Thread() {
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("username", username.getText().toString());
				map.put("password", passowrd.getText().toString());
				map.put("sex", sex.getText().toString());
				map.put("phone", phone.getText().toString());
				map.put("name", name.getText().toString());
				map.put("iden", reg_iden.getText().toString());
				String string = HttpUtil.doPost(HttpUtil.path + "RegServlet",
						map);
				if (string.equals("1")) {
					handler.sendEmptyMessage(0x123);

				} else if(string.equals("0")){
					handler.sendEmptyMessage(0x125);
				} else{
					handler.sendEmptyMessage(0x124);
				}
			};

		}.start();

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				Toast.makeText(RegActivity.this, "注册成功", 1).show();
				startActivity(new Intent(RegActivity.this, LoginActivity.class));
			}
			if (msg.what == 0x124) {
				Toast.makeText(RegActivity.this, "注册失败", 1).show();
			}
			if (msg.what == 0x125) {
				Toast.makeText(RegActivity.this, "身份证已存在", 1).show();
			}

		};
	};

}
