package com.hospital.yuyue;

import com.hospital.fragment.HomeFragment;
import com.hospital.fragment.MainFragment;
import com.hospital.fragment.MineFragment;
import com.hospital.fragment.OrderFragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;

public class MainActivity extends Activity implements OnClickListener{
	private HomeFragment fragment0;
	private MainFragment fragment1;
	private OrderFragment fragment2;
	private MineFragment fragment3;
	private TextView index_,main, mine, order;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		index_ = (TextView) findViewById(R.id.index_);
		main = (TextView) findViewById(R.id.main);
		mine = (TextView) findViewById(R.id.mine);
		order = (TextView) findViewById(R.id.order);
		index_.setOnClickListener(this);
		main.setOnClickListener(this);
		mine.setOnClickListener(this);
		order.setOnClickListener(this);
		setDefaultFragment();
	}
	private void setDefaultFragment() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		fragment0 = new HomeFragment();
		index_.setTextColor(Color.rgb(102, 204, 255));
		main.setTextColor(Color.WHITE);
		mine.setTextColor(Color.WHITE);
		order.setTextColor(Color.WHITE);
		index_.setBackgroundResource(R.drawable.bg_border);
		main.setBackgroundResource(R.drawable.bg_border2);
		mine.setBackgroundResource(R.drawable.bg_border2);
		order.setBackgroundResource(R.drawable.bg_border2);
		transaction.replace(R.id.fragment, new HomeFragment());
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		index_.setTextColor(Color.WHITE);
		main.setTextColor(Color.WHITE);
		mine.setTextColor(Color.WHITE);
		order.setTextColor(Color.WHITE);
		index_.setBackgroundResource(R.drawable.bg_border2);
		main.setBackgroundResource(R.drawable.bg_border2);
		mine.setBackgroundResource(R.drawable.bg_border2);
		order.setBackgroundResource(R.drawable.bg_border2);
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		switch (v.getId()) {
		case R.id.index_:
			fragment0 = new HomeFragment();
			index_.setTextColor(Color.rgb(102, 204, 255));
			index_.setBackgroundResource(R.drawable.bg_border);
			transaction.replace(R.id.fragment, new HomeFragment());
			break;
		case R.id.main:
			fragment1 = new MainFragment();
			main.setTextColor(Color.rgb(102, 204, 255));
			main.setBackgroundResource(R.drawable.bg_border);
			transaction.replace(R.id.fragment, new MainFragment());
			break;

		case R.id.order:
			fragment2 = new OrderFragment();
			order.setTextColor(Color.rgb(102, 204, 255));
			order.setBackgroundResource(R.drawable.bg_border);
			transaction.replace(R.id.fragment, new OrderFragment());
			break;
		case R.id.mine:
			fragment3 = new MineFragment();
			mine.setTextColor(Color.rgb(102, 204, 255));
			mine.setBackgroundResource(R.drawable.bg_border);
			transaction.replace(R.id.fragment, new MineFragment());

		}

		transaction.commit();
	}

}
