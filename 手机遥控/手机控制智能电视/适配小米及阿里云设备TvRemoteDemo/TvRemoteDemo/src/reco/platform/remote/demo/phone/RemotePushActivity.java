package reco.platform.remote.demo.phone;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import reco.platform.remote.demo.R;
import reco.platform.remote.phone.RemoteClient;
import reco.platform.remote.phone.RemoteClient.RemoteCallBack;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 图片投屏
 * 
 * @author quantuMage
 * 
 */
public class RemotePushActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_push);
		findViewById(R.id.bt_send).setOnClickListener(mClickListener);

		copyAssert2File();
	}

	private void copyAssert2File() {

		new Thread() {
			public void run() {

				try {
					InputStream is = getApplicationContext().getAssets().open(
							"wow.jpg");

					FileOutputStream fos = new FileOutputStream(new File(
							getApplicationContext().getFilesDir()
									.getAbsolutePath() + "/test.jpg"));

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

	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			RemoteClient.instance(getApplicationContext()).sendFile(
					getApplicationContext().getFilesDir().getAbsolutePath()
							+ "/test.jpg", new RemoteCallBack() {

						@Override
						public void onSuccess(Object obj) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onFailure(String error) {
							// TODO Auto-generated method stub

						}
					});

		}
	};
}
