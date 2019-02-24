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

import com.hospital.db.HttpUtil;
import com.hospital.util.Office;
import com.hospital.yuyue.R;

public class Order_zi_Fragment_2 extends Fragment{
	private List<Office> list=new ArrayList<Office>();
	private ListView listView;
	private SharedPreferences spf;
	private int uid;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.order_zi_fragment2, null);
		listView=(ListView) view.findViewById(R.id.yifk);
		get();
		return view;
	}
	
	private void get() {
		new Thread() {
			public void run() {
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					spf =getActivity().getSharedPreferences("getuid",0);
					uid=spf.getInt("uid",0);
					map.put("uid",uid);
					String string = HttpUtil.doPost(HttpUtil.path + "GetAllYifk", map);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};

		}.start();

	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				myadapter adapter=new myadapter();
				listView.setAdapter(adapter);

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
