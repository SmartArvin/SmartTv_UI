package reco.platform.remote.demo.phone;

import reco.platform.remote.demo.R;
import reco.platform.remote.phone.RemoteClient;
import reco.platform.remote.tv.TvRemoteListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 简单手柄
 * 
 * @author quantuMage
 * 
 */
public class RemoteHandleActivity extends Activity implements OnClickListener,
		OnTouchListener {

	private TextView tv_dpad;
	private Button bt_shoot;

	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_handle);
		bt_shoot = (Button) findViewById(R.id.bt_shoot);
		tv_dpad = (TextView) findViewById(R.id.tv_dpad);

		bt_shoot.setOnClickListener(this);
		tv_dpad.setOnTouchListener(this);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v.getId() == R.id.tv_dpad) {

			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				RemoteClient.instance(this)
						.sendInputEvent(
								new int[] { 0, (int) event.getX(),
										(int) event.getY() });
			}

		}
		return false;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_shoot:
			RemoteClient.instance(this).sendInputEvent(new int[] { 1, 2 });
			break;

		}

	}
}
