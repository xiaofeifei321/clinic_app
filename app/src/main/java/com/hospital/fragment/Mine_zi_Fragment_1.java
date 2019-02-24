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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hospital.db.HttpUtil;
import com.hospital.util.Lianxi;
import com.hospital.yuyue.AddActivity;
import com.hospital.yuyue.R;

public class Mine_zi_Fragment_1 extends Fragment {
	private List<Lianxi> lianxi = new ArrayList<Lianxi>();
	private ListView listView;
	private SharedPreferences spf;
	private int uid;
	private Button add;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mine_zi_fragment1, null);
		listView = (ListView) view.findViewById(R.id.lianxi);
		add = (Button) view.findViewById(R.id.lianxi_add);
		spf = getActivity().getSharedPreferences("getuid", 0);
		uid = spf.getInt("uid", 0);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AddActivity.class);
				intent.putExtra("uid", uid);
				startActivity(intent);
			}
		});
		on();
		return view;
	}

	public void onStart() {
		super.onStart();
		lianxi.clear();
		get();
	}

	private void on() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setTitle("Ñ¡Ôñ");
				builder.setItems(new String[] { "É¾³ý" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									new Thread() {
										public void run() {
											Map<String, Object> oMap = new HashMap<String, Object>();
											oMap.put("lid", lianxi
													.get(position).getLid());
											String string = HttpUtil.doPost(
													HttpUtil.path + "DeleteLianxi", oMap);
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
							+ "GetAllLianxi", map);
					JSONArray array = new JSONArray(string);
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						Lianxi lx = new Lianxi();
						lx.setLid(object.getInt("lid"));
						lx.setName(object.getString("name"));
						lx.setPhone(object.getString("phone"));
						lx.setUid(object.getInt("uid"));
						lianxi.add(lx);
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
				myadapter adapter = new myadapter();
				listView.setAdapter(adapter);
			}
			if (msg.what == 0x124) {
				Toast.makeText(getActivity(), "É¾³ý³É¹¦", 1).show();
				lianxi.clear();
				get();
			}
		};
	};

	private class myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lianxi.size();
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
					R.layout.lianxi_listview_item, null);
			TextView name = (TextView) view.findViewById(R.id.lianxi_name);
			TextView phone = (TextView) view.findViewById(R.id.lianxi_phone);
			name.setText(lianxi.get(position).getName());
			phone.setText(lianxi.get(position).getPhone());
			return view;
		}

	}

}
