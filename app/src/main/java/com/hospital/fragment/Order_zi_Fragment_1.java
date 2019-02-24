package com.hospital.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hospital.db.HttpUtil;
import com.hospital.util.Office;
import com.hospital.util.User;
import com.hospital.yuyue.R;

public class Order_zi_Fragment_1 extends Fragment {
	private List<Office> list = new ArrayList<Office>();
	private ListView listView;
	private SharedPreferences spf;
	private int uid;
	private List<User> money=new ArrayList<User>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.order_zi_fragment1, null);
		listView = (ListView) view.findViewById(R.id.weifk);
		spf = getActivity().getSharedPreferences("getuid", 0);
		uid = spf.getInt("uid", 0);
		get();
		on();
		money();
		return view;
	}
	private void on() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setTitle("选择");
				builder.setItems(new String[] { "付款" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									new Thread(){
										public void run() {
											Map<String,Object> oMap=new HashMap<String, Object>();
											oMap.put("oid", list.get(position).getOid());
											oMap.put("uid", uid);
											HttpUtil.doPost(HttpUtil.path+"WeiForYi", oMap);
											
										};
										
									}.start();
									new Thread(){
										public void run() {
											Map<String,Object> oMap=new HashMap<String, Object>();
											oMap.put("uid",uid);
											oMap.put("money", money.get(position).getMoney()-list.get(position).getNeed());
											String string=HttpUtil.doPost(HttpUtil.path+"AddMoney", oMap);
											if (string.equals("true")) {
												handler.sendEmptyMessage(0x124);
											}
											
										};
										
									}.start();
									
									break;
								}

							}
						});
				builder.show();
			}
		});

	}

	private void get() {
		new Thread() {
			public void run() {
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", uid);
					String string = HttpUtil.doPost(HttpUtil.path
							+ "GetAllWeifk", map);
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
						list.add(office);
					}
					handler.sendEmptyMessage(0x123);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			};

		}.start();

	}
	private void money() {
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
							user.setMoney(object.getInt("money"));
							user.setName(object.getString("name"));
							user.setPassword(object.getString("password"));
							user.setPhone(object.getString("phone"));
							user.setSex(object.getString("sex"));
							user.setUid(object.getInt("uid"));
							user.setUsername(object.getString("username"));
							money.add(user);
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
				myadapter adapter = new myadapter();
				listView.setAdapter(adapter);
			}
			if (msg.what==0x124) {
				Toast.makeText(getActivity(), "付款成功",1).show();
				list.clear();
				get();
			}
		};
	};

	private class myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.keshi_listview_item, null);
			TextView oname = (TextView) view.findViewById(R.id.oname);
			TextView docname = (TextView) view.findViewById(R.id.docname);
			TextView phone = (TextView) view.findViewById(R.id.phone);
			oname.setText(list.get(position).getOname());
			docname.setText(list.get(position).getDocname());
			phone.setText(list.get(position).getPhone());
			return view;
		}
	}
}
