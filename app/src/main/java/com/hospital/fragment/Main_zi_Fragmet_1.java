package com.hospital.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hospital.db.HttpUtil;
import com.hospital.util.Guide;
import com.hospital.yuyue.R;

public class Main_zi_Fragmet_1 extends Fragment {
	private TextView menzhen, zhuyuan, tijian;
	private Button menButton,zhuButton,tiButton;
	private List<Guide> list = new ArrayList<Guide>();
	private int pd1 = 0;
	private int pd2 = 0;
	private int pd3= 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_zi_fragment1, null);
		menzhen = (TextView) view.findViewById(R.id.menzhen);
		zhuyuan = (TextView) view.findViewById(R.id.zhuyuan);
		tijian = (TextView) view.findViewById(R.id.tijian);
		menButton=(Button) view.findViewById(R.id.menzhenon);
		zhuButton=(Button) view.findViewById(R.id.zhuyuanon);
		tiButton=(Button) view.findViewById(R.id.tijianon);
		menzhen.setVisibility(View.GONE);
		zhuyuan.setVisibility(View.GONE);
		tijian.setVisibility(View.GONE);
		get();
		onc();
		return view;
	}
	@Override
	public void onStart() {
		super.onStart();
		get();
	}
	private void onc(){
		menButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (pd1 == 0) {
					menzhen.setVisibility(View.VISIBLE);
					pd1 = 1;
					return;
				} else if (pd1 == 1) {
					menzhen.setVisibility(View.GONE);
					pd1 = 0;
					return;
				}
			}
		});
		zhuButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (pd2 == 0) {
					zhuyuan.setVisibility(View.VISIBLE);
					pd2 = 1;
					return;
				} else if (pd2 == 1) {
					zhuyuan.setVisibility(View.GONE);
					pd2 = 0;
					return;
				}
			}
		});
		tiButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (pd3 == 0) {
					tijian.setVisibility(View.VISIBLE);
					pd3 = 1;
					return;
				} else if (pd3 == 1) {
					tijian.setVisibility(View.GONE);
					pd3 = 0;
					return;
				}
			}
		});
		
	}


	private void get() {
		new Thread() {
			public void run() {

				try {
					Map<String, Object> map = new HashMap<String, Object>();
					String string = HttpUtil.doPost(HttpUtil.path
							+ "GetAllGuide", map);
					JSONArray array = new JSONArray(string);
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						Guide guide = new Guide();
						guide.setGid(object.getInt("gid"));
						guide.setMenzhen(object.getString("menzhen"));
						guide.setTijian(object.getString("tijian"));
						guide.setZhuyuan(object.getString("zhuyuan"));
						list.add(guide);
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
				menzhen.setText(list.get(0).getMenzhen());
				zhuyuan.setText(list.get(0).getZhuyuan());
				tijian.setText(list.get(0).getTijian());
			}
		};
	};
}
