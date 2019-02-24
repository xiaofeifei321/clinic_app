package com.hospital.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hospital.db.HttpUtil;
import com.hospital.util.Office;
import com.hospital.yuyue.DocActivity;
import com.hospital.yuyue.R;

public class Main_zi_Fragmet_2 extends Fragment {
	private ListView listView;
	private List<Office> list = new ArrayList<Office>();
	private EditText sousuo;
	private Button sousuobt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_zi_fragment2, null);
		listView = (ListView) view.findViewById(R.id.keshi);
		sousuo=(EditText) view.findViewById(R.id.sousuo);
		sousuobt=(Button) view.findViewById(R.id.sousuobt);
		get();
		on();
		return view;
	}
	private void on(){
		sousuo.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				list.clear();
				get();
				
			}
		});
		sousuobt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				list.clear();
				new Thread(){
					public void run() {
						Map<String, Object> oMap=new HashMap<String, Object>();
						oMap.put("sousuo",sousuo.getText().toString());
						String string=HttpUtil.doPost(HttpUtil.path+"Sousuo", oMap);
						try {
							JSONArray array=new JSONArray(string);
							for (int i = 0; i < array.length(); i++) {
								JSONObject object = array.getJSONObject(i);
								Office office = new Office();
								office.setContent(object.getString("content"));
								office.setDocname(object.getString("docname"));
								office.setNeed(object.getInt("need"));
								office.setOid(object.getInt("oid"));
								office.setOntime(object.getString("ontime"));
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
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(getActivity(), DocActivity.class);
				intent.putExtra("oid", list.get(position).getOid());
				startActivity(intent);
				
			}
		});
		
		
	}

	private void get() {
		new Thread() {
			public void run() {
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					String string = HttpUtil.doPost(HttpUtil.path + "GetAllOffice", map);
					JSONArray array = new JSONArray(string);
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						Office office = new Office();
						office.setContent(object.getString("content"));
						office.setDocname(object.getString("docname"));
						office.setNeed(object.getInt("need"));
						office.setOntime(object.getString("ontime"));
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
			TextView ontime = (TextView) view.findViewById(R.id.ontime);
			oname.setText(list.get(position).getOname());
			docname.setText(list.get(position).getDocname());
			phone.setText(list.get(position).getPhone());
			ontime.setText("×øÕïÊ±¼ä:"+list.get(position).getOntime());
			return view;
		}
	}
}
