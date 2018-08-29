package reco.platform.remote.demo.phone;

import reco.platform.remote.demo.R;
import reco.platform.remote.phone.RemoteClient;
import reco.platform.remote.phone.RemoteClient.RemoteCallBack;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 手机电端数据互传
 * 
 * @author quantuMage
 * 
 */
public class RemoteRequestActivity extends Activity {

	private String apkName = "demo.apk";
	private Button bt_send;
	private EditText et_msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_request);

		bt_send = (Button) findViewById(R.id.bt_send);
		et_msg = (EditText) findViewById(R.id.et_msg);

		bt_send.setOnClickListener(mClickListener);

	}

	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.bt_send:
				RemoteClient.instance(getApplicationContext()).sendText(
						et_msg.getText().toString(), new RemoteCallBack() {

							@Override
							public void onSuccess(Object obj) {

								Toast.makeText(getApplicationContext(),
										obj.toString(), 0).show();

							}

							@Override
							public void onFailure(String error) {
								Toast.makeText(getApplicationContext(), error,
										0).show();

							}
						});
				break;
			}

		}
	};
}
