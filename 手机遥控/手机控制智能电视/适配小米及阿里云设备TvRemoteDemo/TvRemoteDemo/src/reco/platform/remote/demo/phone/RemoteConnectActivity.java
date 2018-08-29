package reco.platform.remote.demo.phone;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import reco.platform.remote.demo.R;
import reco.platform.remote.entity.ConnectInfo;
import reco.platform.remote.phone.RemoteClient;
import reco.platform.remote.phone.RemoteConnectListener;
import reco.platform.remote.phone.RemoteHandler;
import reco.platform.remote.tv.TvRemoteListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 第一二种连接 需电视端支持
 * 
 * 第三种 扫描 无需电视端支持 但只能控制 不能互传数据
 * 
 * @author quantuMage
 * 
 */
public class RemoteConnectActivity extends Activity {

	private String TAG = "RemoteConnectActivity";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_connect);
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

		et_ip = (EditText) findViewById(R.id.et_ip);
		et_port = (EditText) findViewById(R.id.et_port);
		bt_scan = (Button) findViewById(R.id.bt_scan);
		findViewById(R.id.bt_ip).setOnClickListener(clickListener);
		findViewById(R.id.bt_udp).setOnClickListener(clickListener);
		bt_scan.setOnClickListener(clickListener);

		// 局域网IP段扫描可用设备 然后据设备类型连接
		final ListView lv_devices = (ListView) findViewById(R.id.lv_device_list);
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1);

		lv_devices.setAdapter(arrayAdapter);
		// 设置遥控监听
		RemoteClient.instance(this).setRemoteListener(
				new RemoteConnectListener() {

					@Override
					public void onScanFinish() {
						Log.e(TAG,
								"扫描结束,发现设备数="
										+ RemoteClient
												.instance(
														getApplicationContext())
												.getDeviceList().size());

						// 加载到列表
						lv_devices.setAdapter(arrayAdapter);
						bt_scan.setText("扫描结束");

					}

					@Override
					public void onDeviceFound(ConnectInfo conn) {
						Log.e("TAG", conn.getAddress());
						arrayAdapter.add(conn.getDeviceId() + ":"
								+ conn.getAddress());

					}

					public void onConnectStateChange(ConnectInfo conn, int state) {
						switch (state) {
						case RemoteHandler.CONNECT_SUCCESS:
							Toast.makeText(getApplicationContext(),
									"Socket成功连接", 1).show();
							break;
						case RemoteHandler.STATE_REMOTE_USEFUL:
							Toast.makeText(getApplicationContext(), "按键遥控就绪", 1)
									.show();
							break;
						}
						jump();

					}

					public void onRemoteReceive(ConnectInfo conn, String msg) {
						Toast.makeText(getApplicationContext(),
								"收到电视端消息=" + msg, 0).show();

					}
				});

		lv_devices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String itemStr = arrayAdapter.getItem(position);
				String ip = itemStr.substring(itemStr.lastIndexOf(":") + 1);
				RemoteClient.instance(getApplicationContext()).connect(ip);
			}
		});

	}

	private void jump() {
		startActivity(new Intent(this, RemoteInstallActivity.class));
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.bt_ip:
				String ip = et_ip.getText().toString();
				int port = Integer.parseInt(et_port.getText().toString());
				// 指定ip 端口连接
				RemoteClient.instance(getApplicationContext())
						.connect(ip, port);
				break;
			case R.id.bt_udp:
				// 当电视端运行时 通过UDP 获取设备信息
				RemoteClient.instance(getApplicationContext()).connectByUdp();
				break;
			case R.id.bt_scan:
				// 开始扫描
				RemoteClient.instance(getApplicationContext()).startScan();
				bt_scan.setText("正在扫描IP");
				break;
			}

		}
	};
	private EditText et_ip, et_port;
	private Button bt_scan;
}
