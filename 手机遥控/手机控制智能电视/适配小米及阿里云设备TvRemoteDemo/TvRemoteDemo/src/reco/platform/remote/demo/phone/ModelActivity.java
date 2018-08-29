package reco.platform.remote.demo.phone;

import reco.platform.remote.demo.R;
import reco.platform.remote.phone.RemoteClient;
import reco.platform.remote.phone.RemoteHandler;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ModelActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		load();
		connect();
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

	private void connect() {
		// RemoteClient.instance(this).connectAoto();
		// RemoteClient.instance(this).setRemoteListener(new RemoteListener() {
		//
		// @Override
		// public void onRemoteReceive(String client, String msg, int type) {
		// Toast.makeText(getApplicationContext(), msg, 0).show();
		//
		// }
		//
		// @Override
		// public void onConnectStateChange(ConnectInfo conn, int state) {
		// if (RemoteHandler.STATE_REMOTE_USEFUL == state) {
		// Toast.makeText(getApplicationContext(), "遥控已连接", 0).show();
		// }
		//
		// }
		//
		// @Override
		// public void onDeviceFound(ConnectInfo conn) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onScanFinish() {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}

	private void load() {

		findViewById(R.id.bt_remote).setOnClickListener(mClickListener);
		findViewById(R.id.bt_request).setOnClickListener(mClickListener);
		findViewById(R.id.bt_push_image).setOnClickListener(mClickListener);
		findViewById(R.id.bt_handle).setOnClickListener(mClickListener);

	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_remote:
				startActivity(new Intent(ModelActivity.this,
						RemoteActivity.class));
				break;
			case R.id.bt_request:
				startActivity(new Intent(ModelActivity.this,
						RemoteRequestActivity.class));
				break;
			case R.id.bt_push_image:
				startActivity(new Intent(ModelActivity.this,
						RemotePushActivity.class));
				break;
			case R.id.bt_handle:
				startActivity(new Intent(ModelActivity.this,
						RemoteHandleActivity.class));
				break;

			}

		}
	};
}
