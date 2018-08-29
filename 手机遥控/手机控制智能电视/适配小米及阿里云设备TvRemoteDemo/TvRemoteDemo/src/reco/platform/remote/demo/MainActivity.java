package reco.platform.remote.demo;

import reco.platform.remote.demo.phone.RemoteConnectActivity;
import reco.platform.remote.demo.tv.TvActivity;

import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	private String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		load();
	}

	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}

	private void load() {

		findViewById(R.id.bt_tv).setOnClickListener(mClickListener);
		findViewById(R.id.bt_phone).setOnClickListener(mClickListener);

	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_tv:
				startActivity(new Intent(MainActivity.this, TvActivity.class));
				break;
			case R.id.bt_phone:
				startActivity(new Intent(MainActivity.this,
						RemoteConnectActivity.class));
				break;

			}

		}
	};

}
