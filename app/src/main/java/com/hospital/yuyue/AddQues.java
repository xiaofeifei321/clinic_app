package com.hospital.yuyue;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hospital.db.HttpUtil;

public class AddQues extends Activity {
	private EditText name,phone;
	private Button add;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_ques);
		name=(EditText) findViewById(R.id.lianxi_add_name);
		phone=(EditText) findViewById(R.id.lianxi_add_phone);
		add=(Button) findViewById(R.id.qradd);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(){
					public void run() {
						Map<String, Object>map=new HashMap<String, Object>();
						Intent intent=getIntent();
						map.put("name", name.getText().toString());
						map.put("content",phone.getText().toString());
						map.put("uid",intent.getIntExtra("uid",0));
						String string=HttpUtil.doPost(HttpUtil.path+"AddQues",map);
						if (string.equals("true")) {
							handler.sendEmptyMessage(0x123);
						}
					};
					
				}.start();
				
			}
		});
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				Toast.makeText(AddQues.this, "Ìí¼Ó³É¹¦", 1).show();
				finish();
			}
		};
	};


}
