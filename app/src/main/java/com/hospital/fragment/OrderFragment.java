package com.hospital.fragment;

import com.hospital.yuyue.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderFragment extends Fragment implements OnClickListener {
	private Order_zi_Fragment_1 fragment1;
	private Order_zi_Fragment_2 fragment2;
	private TextView tView1, tView2;
	private LinearLayout layout1, layout2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.order_fragment, null);
		tView1 = (TextView) view.findViewById(R.id.order_ly_tv_1);
		tView2 = (TextView) view.findViewById(R.id.order_ly_tv_2);
		layout1 = (LinearLayout) view.findViewById(R.id.order_ly_1);
		layout2 = (LinearLayout) view.findViewById(R.id.order_ly_2);
		tView1.setOnClickListener(this);
		tView2.setOnClickListener(this);
		setDefaultFragment();
		return view;
	}

	private void setDefaultFragment() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		fragment1 = new Order_zi_Fragment_1();
		layout1.setBackgroundColor(Color.rgb(102, 204, 255));
		transaction.replace(R.id.content3, new Order_zi_Fragment_1());
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		layout1.setBackgroundColor(Color.WHITE);
		layout2.setBackgroundColor(Color.WHITE);
		switch (v.getId()) {
		case R.id.order_ly_tv_1:
			fragment1 = new Order_zi_Fragment_1();
			layout1.setBackgroundColor(Color.rgb(102, 204, 255));
			transaction.replace(R.id.content3, new Order_zi_Fragment_1());
			break;
		case R.id.order_ly_tv_2:
			fragment2 = new Order_zi_Fragment_2();
			layout2.setBackgroundColor(Color.rgb(102, 204, 255));
			transaction.replace(R.id.content3, new Order_zi_Fragment_2());
			break;
		}

		transaction.commit();
	}

}
