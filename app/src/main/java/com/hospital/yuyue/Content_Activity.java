package com.hospital.yuyue;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hospital.db.HttpUtil;
import com.hospital.util.Events;





import android.location.GpsStatus.NmeaListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Content_Activity extends Activity {
	private String mnameString,banner_name;
	private String mnamestr, mpicstr, mtypestr, usernamestr, mtimestr,
			mcontentstr, mmakingsstr, mstepstr, midstr, zanstr;
	private ImageView mpic;
	private TextView mname, mtype, username, mtime, mcontent, mmakings, mstep,
			con_zan, back;
private String file_link,str;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content_layout);
		mpic = (ImageView) findViewById(R.id.con_mpic);
		mname = (TextView) findViewById(R.id.con_mname);
		back = (TextView) findViewById(R.id.back);
		mtype = (TextView) findViewById(R.id.con_mtype);
		username = (TextView) findViewById(R.id.con_username);
		mtime = (TextView) findViewById(R.id.con_mtime);
		mcontent = (TextView) findViewById(R.id.con_mcontent);
		Intent intent = getIntent();
		mnameString = intent.getStringExtra("mname");
		banner_name = intent.getStringExtra("banner_name");
		getcontent();
		
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getcontent();

	}


	public void searchfh(View v) {
		this.finish();
	}

	public void getcontent() {
		new Thread() {
			public void run() {
				try {
					String keyword ="";
					if(mnameString==null&&banner_name!=null){
						keyword=banner_name;
					}else{
						keyword=mnameString;
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("mname", keyword);
					String str = HttpUtil.doPost(HttpUtil.pathstr
							+ "GetContentSrevlet", map);
					JSONArray jsonArray = new JSONArray(str);
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					Events sc = new Events();
					midstr = jsonObject.getString("mid");
					mnamestr = jsonObject.getString("mname");
					mpicstr = jsonObject.getString("mpic");
					usernamestr = jsonObject.getString("username");
					mtypestr = jsonObject.getString("mtype");
					mtimestr = jsonObject.getString("mtime");
					mcontentstr = jsonObject.getString("mcontent");
					handler.sendEmptyMessage(0x224);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x224) {
				new myasy(mpic).execute(HttpUtil.picstr + mpicstr);
				mname.setText(mnamestr);
				username.setText(usernamestr);
				mtype.setText(mtypestr);
				mtime.setText(mtimestr);
				mcontent.setText(mcontentstr);
				
			}
		
				
		};
	};

	private class myasy extends AsyncTask<String, Void, Bitmap> {
		ImageView ivg;

		public myasy(ImageView ivg) {
			// TODO Auto-generated constructor stub
			this.ivg = ivg;
		}

		@Override
		protected Bitmap doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			Bitmap bitmap = null;
			try {
				URL url = new URL(arg0[0]);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				InputStream isInputStream = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(isInputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ivg.setImageBitmap(result);
		}
	}

}