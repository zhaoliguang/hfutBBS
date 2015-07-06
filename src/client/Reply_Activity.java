package client;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;

import config.Url_Config;
import tools.Json_Data;
import tools.Submit;
import cn.edu.hfut.bbs.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Reply_Activity extends Activity {
	
	private Button btn_submit;
	private EditText text;
	
	private String tid;
	private String fid;
	private String pid;
	
	private User user;
	private String type;
		
	public Reply_Activity() {
		btn_submit = null;
		text = null;
		
		tid = null;
		fid = null;
		pid = null;
		
		user = null;
		type = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);
		Bundle extras = getIntent().getExtras();
		tid = extras.getString("tid");
		fid = extras.getString("fid");
		pid = extras.getString("pid");
		type = extras.getString("type");
		user = (User)getIntent().getSerializableExtra("user");
		

		btn_submit = (Button)findViewById(R.id.submit);
		text       = (EditText)findViewById(R.id.test);
		
		
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				btn_submit.setClickable(false);
				new Handler().postDelayed(new Runnable() {
					
					public void run() {
						String reply_message = text.getText().toString();
						List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
						params.add(new BasicNameValuePair("fid", fid));
						params.add(new BasicNameValuePair("tid", tid));
						params.add(new BasicNameValuePair("pid", pid));
						params.add(new BasicNameValuePair("message", reply_message));
						params.add(new BasicNameValuePair("type", type));
						Json_Data result = Submit.submit(user, params, Url_Config.post_newreply, null);
						
						if (null == result) {
							Toast.makeText(getApplicationContext(), "¡¨Ω” ß∞‹", Toast.LENGTH_SHORT).show();
						} else if (!result.get_status()) {
							Toast.makeText(getApplicationContext(), result.get_info(), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), result.get_info(), Toast.LENGTH_SHORT).show();
							finish();
						}
						btn_submit.setClickable(true);
					}
				}, 2000);
			}
			
		});
	}
}