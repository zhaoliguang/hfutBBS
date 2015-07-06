package client;

import cn.edu.hfut.bbs.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity_layout_1_1);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = new Intent(SplashActivity.this,
						Login_Activity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}
		}, SPLASH_DISPLAY_LENGHT);

	}
}