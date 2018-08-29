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
 * 虚拟遥控器
 * 
 * @author quantuMage
 * 
 */
public class RemoteActivity extends Activity implements OnClickListener,
		OnTouchListener {

	private TextView tv_dpad;
	private TextView tv_home;
	private TextView tv_back;
	private TextView tv_menu;
	private TextView tv_power;

	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_remote);
		tv_power = (TextView) findViewById(R.id.tv_power);
		tv_home = (TextView) findViewById(R.id.tv_home);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_menu = (TextView) findViewById(R.id.tv_menu);
		tv_dpad = (TextView) findViewById(R.id.tv_dpad);

		tv_power.setOnClickListener(this);
		tv_home.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		tv_menu.setOnClickListener(this);
		tv_dpad.setOnTouchListener(this);

		gestureDetector = new GestureDetector(this, new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {

				RemoteClient.instance(getApplicationContext()).sendKeyEvent(
						KeyEvent.KEYCODE_DPAD_CENTER);
				return false;
			}

			@Override
			public void onShowPress(MotionEvent arg0) {
			}

			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1,
					float arg2, float arg3) {

				return false;
			}

			@Override
			public void onLongPress(MotionEvent arg0) {
			}

			@Override
			public boolean onFling(MotionEvent e, MotionEvent e2, float vx,
					float vy) {

				float dx = Math.abs(e.getX() - e2.getX());
				float dy = Math.abs(e.getY() - e2.getY());

				int keyCode = -1;
				// 妯????
				if (dx > dy) {

					if (vx > 1000) {
						keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
					} else if (vx < -1000) {
						keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
					}

				} else {
					// 绾靛??
					if (vy > 1000) {
						keyCode = KeyEvent.KEYCODE_DPAD_DOWN;
					} else if (vy < -1000) {
						keyCode = KeyEvent.KEYCODE_DPAD_UP;
					}

				}
				if (keyCode != -1) {
					RemoteClient.instance(getApplicationContext())
							.sendKeyEvent(keyCode);
				}

				return false;
			}

			@Override
			public boolean onDown(MotionEvent arg0) {
				return true;
			}
		});

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v.getId() == R.id.tv_dpad) {
			return gestureDetector.onTouchEvent(event);
		}
		return false;
	}

	@Override
	public void onClick(View v) {

		int keyCode = 0;
		switch (v.getId()) {
		case R.id.tv_home:
			keyCode = KeyEvent.KEYCODE_HOME;
			break;
		case R.id.tv_back:
			keyCode = KeyEvent.KEYCODE_BACK;
			break;
		case R.id.tv_menu:
			keyCode = KeyEvent.KEYCODE_MENU;
			break;
		case R.id.tv_power:
			keyCode = KeyEvent.KEYCODE_POWER;
			break;

		}
		RemoteClient.instance(this).sendKeyEvent(keyCode);

	}
}
