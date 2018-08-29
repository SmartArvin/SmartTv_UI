package reco.platform.remote.demo.tv;

import reco.platform.remote.demo.R;
import reco.platform.remote.entity.ClientInfo;
import reco.platform.remote.entity.ConnectPool;
import reco.platform.remote.tv.TvRemoteHandler;
import reco.platform.remote.tv.TvRemoteListener;
import reco.platform.remote.tv.TvRemoteServer;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TvActivity extends Activity {
	private final static String TAG = "TvRemoteActivity";
	private ImageView iv_target;
	private int screenWidth, screenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tv);
		rl_rool = (RelativeLayout) findViewById(R.id.rl_root);
		iv_target = (ImageView) findViewById(R.id.iv_target);
		tv_connect = (TextView) findViewById(R.id.tv_connect);

		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();

		TvRemoteServer.instance(this).launcher();
		TvRemoteServer.instance(this).setRemoteListener(new TvRemoteListener() {

			public Object onRemoteRequest(String ip, String content) {
				Toast.makeText(getApplicationContext(), content, 0).show();

				// 向手机端响应消息

				return "这是返回给手机端的消息";
			}

			@Override
			public void onRemoteInput(String ip, int[] values) {

				// values 为参数组 此处0 为触摸方向 1 为射击
				switch (values[0]) {
				case 0:
					// 此处做一个简单映射
					int x = values[1] * 3;
					int y = values[2];
					if (x > screenWidth) {
						x = screenWidth;
					}
					if (y > screenHeight) {
						y = screenHeight;
					}
					moveCube(x, y);
					break;
				case 1:
					flashCube();
					break;
				}

			}

			public Object onRemoteFile(String ip, String filePath) {
				Bitmap bmp = BitmapFactory.decodeFile(filePath);
				rl_rool.setBackgroundDrawable(new BitmapDrawable(bmp));

				return null;
			}

			public void onConnectStateChange(int state, ConnectPool clientPool,
					ClientInfo client) {

				switch (state) {
				case TvRemoteHandler.CONNECT_WAIT:
					tv_connect.setText(clientPool.getLocalAddress() + ":"
							+ clientPool.getSocketPort());

					Toast.makeText(getApplicationContext(), "服务端等待连接", 0)
							.show();
					break;

				case TvRemoteHandler.CONNECT_SUCCESS:

					Toast.makeText(
							getApplicationContext(),
							"连接新设备=" + client.getAddress() + ":"
									+ client.getPort(), 0).show();
					break;

				}

			}

		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		moveCube(keyCode);
		return super.onKeyDown(keyCode, event);
	}

	private int l, t;
	private RelativeLayout rl_rool;
	private TextView tv_connect;

	/**
	 * 根据遥控按键控制方块轨迹
	 * 
	 * @param keyCode
	 */
	private void moveCube(int keyCode) {

		if (l == 0) {
			l = iv_target.getLeft();
			t = iv_target.getTop();
		}

		iv_target.clearAnimation();
		AnimatorSet animatorSet = new AnimatorSet();
		ObjectAnimator anim = null;

		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			anim = ObjectAnimator.ofFloat(iv_target, "x", l, l -= 100);

			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			anim = ObjectAnimator.ofFloat(iv_target, "x", l, l += 100);
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			anim = ObjectAnimator.ofFloat(iv_target, "y", t, t -= 100);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			anim = ObjectAnimator.ofFloat(iv_target, "y", t, t += 100);
			break;
		}
		animatorSet.play(anim);
		animatorSet.setDuration(300);
		animatorSet.start();
	}

	/**
	 * 根据手机端传来坐标 移动 方块
	 * 
	 * @param keyCode
	 */
	private void moveCube(int x, int y) {
		Log.e(TAG, screenWidth + "---" + screenHeight + "---" + x + "---" + x);
		int l = x;
		int r = x + 100;
		int t = y;
		int b = y + 100;
		iv_target.layout(l, t, r, b);

	}

	/**
	 * 控制方块放大缩小
	 */
	private void flashCube() {
		iv_target.clearAnimation();
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(100);
		ObjectAnimator lX = ObjectAnimator.ofFloat(iv_target, "ScaleX", 1f,
				3.0f);
		ObjectAnimator lY = ObjectAnimator.ofFloat(iv_target, "ScaleY", 1f,
				3.0f);

		ObjectAnimator sX = ObjectAnimator.ofFloat(iv_target, "ScaleX", 1f);
		ObjectAnimator sY = ObjectAnimator.ofFloat(iv_target, "ScaleY", 1f);

		animatorSet.play(lX).with(lY);
		animatorSet.play(lX).before(sX);
		animatorSet.play(sX).with(sY);
		animatorSet.start();

	}

}
