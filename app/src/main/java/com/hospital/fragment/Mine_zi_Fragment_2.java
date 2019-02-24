package com.hospital.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hospital.db.HttpUtil;
import com.hospital.util.User;
import com.hospital.yuyue.ChongzhiActivity;
import com.hospital.yuyue.LoginActivity;
import com.hospital.yuyue.R;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Mine_zi_Fragment_2 extends Fragment implements OnClickListener {
	private TextView name, sex, phone, money, password, qrxg, czzx, tcdl,mine_iden,ques;
	private SharedPreferences spf;
	List<User> list = new ArrayList<User>();
	private int uid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mine_zi_fragment2, null);
		name = (TextView) view.findViewById(R.id.mine_name);
		sex = (TextView) view.findViewById(R.id.mine_sex);
		phone = (TextView) view.findViewById(R.id.mine_phone);
		mine_iden = (TextView) view.findViewById(R.id.mine_iden);
		ques = (TextView) view.findViewById(R.id.ques);
		money = (TextView) view.findViewById(R.id.mine_money);
		password = (TextView) view.findViewById(R.id.mine_password);
		qrxg = (TextView) view.findViewById(R.id.qrxg);
		czzx = (TextView) view.findViewById(R.id.czzx);
		tcdl = (TextView) view.findViewById(R.id.tcdl);
		qrxg.setOnClickListener(this);
		czzx.setOnClickListener(this);
		tcdl.setOnClickListener(this);
		ques.setOnClickListener(this);
		spf = getActivity().getSharedPreferences("getuid", 0);
		uid = spf.getInt("uid", 0);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		get();
	}

	private void get() {
		new Thread() {
			public void run() {

				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", uid);
					String string = HttpUtil.doPost(HttpUtil.path + "GetOneUser", map);
					if(string != null){
						JSONArray array = new JSONArray(string);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);
							User user = new User();
							user.setIden(object.getString("iden"));
							user.setMoney(object.getInt("money"));
							user.setName(object.getString("name"));
							user.setPassword(object.getString("password"));
							user.setPhone(object.getString("phone"));
							user.setSex(object.getString("sex"));
							user.setUid(object.getInt("uid"));
							user.setUsername(object.getString("username"));
							list.add(user);
						}
						handler.sendEmptyMessage(0x123);
					}
				
				} catch (JSONException e) {
					e.printStackTrace();
				}

			};

		}.start();

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				name.setText(list.get(0).getName());
				sex.setText(list.get(0).getSex());
				mine_iden.setText(list.get(0).getIden());
				phone.setText(list.get(0).getPhone());
				password.setText(list.get(0).getPassword());
				money.setText(list.get(0).getMoney() + "");
			}
			if (msg.what==0x124) {
				SharedPreferences spf1;
				spf1= getActivity().getSharedPreferences("itcast", 0);
				Editor editor = spf1.edit();
				editor.putBoolean("flag", false);
				editor.commit();
				Toast.makeText(getActivity(), "修改成功", 1).show();
				Toast.makeText(getActivity(), "登录过期，请重新登录", 1).show();
				startActivity(new Intent(getActivity(), LoginActivity.class));
				getActivity().finish();
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qrxg:
			new Thread(){
				public void run() {
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("uid",uid);
					map.put("name", name.getText().toString());
					map.put("sex", sex.getText().toString());
					map.put("phone", phone.getText().toString());
					map.put("password", password.getText().toString());
					String string= HttpUtil.doPost(HttpUtil.path+"UpdateUser",map);
					if (string.equals("true")) {
						handler.sendEmptyMessage(0x124);
					}
				};
				
			}.start();
			break;
		case R.id.czzx:
			Intent intent=new Intent(getActivity(), ChongzhiActivity.class);
			intent.putExtra("uid", uid);
			startActivity(intent);
			break;
		case R.id.ques:
			Intent intent1=new Intent(getActivity(), QuesActivity.class);
			startActivity(intent1);
			break;
		case R.id.tcdl:
			SharedPreferences spf1;
			spf1= getActivity().getSharedPreferences("itcast", 0);
			Editor editor = spf1.edit();
			editor.putBoolean("flag", false);
			editor.commit();
			Toast.makeText(getActivity(), "退出成功", 1).show();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			getActivity().finish();
			break;

		}

	}
}
