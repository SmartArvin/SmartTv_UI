package reco.platform.remote.demo.phone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import reco.platform.remote.demo.R;
import reco.platform.remote.phone.RemoteClient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 远程安装电视端 暂只支持ADB设备
 * 
 * @author quantuMage
 * 
 */
public class RemoteInstallActivity extends Activity {
	private String TAG = "RemoteInstallActivity";
	private String apkName = "demo";
	private Button bt_install;
	private Button bt_jump;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_install);

		bt_install = (Button) findViewById(R.id.bt_install);
		bt_jump = (Button) findViewById(R.id.bt_jump);
		bt_install.setOnClickListener(mClickListener);
		bt_jump.setOnClickListener(mClickListener);

		// 将电视端apk自assert文件复制到file文件中
		try {
			copyAssert2File(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void copyAssert2File() {

		new Thread() {
			public void run() {

				try {
					InputStream is = getApplicationContext().getAssets().open(
							"TvRemoteDemo.apk");

					FileOutputStream fos = new FileOutputStream(
							new File(getApplicationContext().getFilesDir()
									.getAbsolutePath() + "/" + apkName + ".apk"));

					byte[] b = new byte[1024];
					while (is.read(b, 0, b.length) != -1) {
						fos.write(b, 0, b.length);
					}
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			};
		}.start();
	}

	public void copyAssert2File(Context context) throws Exception {

		File file = new File(context.getFilesDir().getAbsolutePath() + "/"
				+ apkName + ".apk");
		if (!file.exists()) {
			InputStream is = context.getClass().getResourceAsStream(
					"/assets/" + "TvRemoteDemo.apk");

			FileOutputStream fos;
			try {
				fos = context.openFileOutput(apkName + ".apk",
						Context.MODE_WORLD_READABLE);
				while (true) {
					byte[] buffer = new byte[200];
					int numread;
					try {
						numread = is.read(buffer);
						if (numread == -1) {
							break;
						}
						fos.write(buffer, 0, numread);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					fos.close();
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_install:

				String apkPath = getApplicationContext().getFilesDir()
						.getAbsolutePath() + "/" + apkName + ".apk";

				File file = new File(apkPath);
				if (file.exists()) {
					Log.e(TAG, apkPath + "---" + file.length());
					RemoteClient.instance(getApplicationContext()).install(
							apkPath);
				}

				break;
			case R.id.bt_jump:
				startActivity(new Intent(getApplicationContext(),
						ModelActivity.class));
				break;
			}

		}
	};
}
