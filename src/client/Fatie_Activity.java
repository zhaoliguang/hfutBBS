package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import android.widget.Toast;

public class Fatie_Activity extends Activity {

	private String fid;
	private Button sure;
	private User user;
	private EditText subject;
	private Spinner type;
	private EditText message;
	private String typeid;
	private Json_Data type_json;
	
	public Fatie_Activity() {
		fid = null;
		sure = null;
		user = null;
		subject = null;
		type = null;
		message = null;
		typeid = null;
		type_json = null;
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/// 隐藏标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fatie);
		
		fid = getIntent().getExtras().getString("fid");
		user = (User)getIntent().getSerializableExtra("user");
		subject = (EditText)findViewById(R.id.fatie_subject);
		type = (Spinner)findViewById(R.id.fatie_type);
		message = (EditText)findViewById(R.id.fatie_message);
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("fid", fid));
		type_json = Submit.submit(user, params, Url_Config.get_type, "typename");
		List<String> m = new ArrayList<String>();
		
		if (null == type_json) {
			m.add("获取信息失败");
			finish();
		} else if (!type_json.get_status()) {
			m.add("无分类");
		} else {
			HashMap< String, HashMap<String, String> > data = type_json.get_data();

			Iterator<Entry<String, HashMap<String, String>>> it = data.entrySet().iterator();
			
			while(it.hasNext()){
				java.util.Map.Entry<String, HashMap<String, String>> entry = (java.util.Map.Entry<String, HashMap<String, String>>)it.next();
				
				HashMap<String, String> tmp_map = (HashMap<String, String>)entry.getValue();
				m.add(tmp_map.get("typename"));
			}
			
			
			
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m);  
        
        //设置下拉列表的风格  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
          
        //将adapter 添加到spinner中  
        type.setAdapter(adapter);
		
		
		sure = (Button)findViewById(R.id.sure);
		
		sure.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				typeid = "0";
				
				sure.setText("发表中...");
				sure.setClickable(false);
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						String type_name = type.getSelectedItem().toString();
						Log.i("typename", type_name);
						
						if (type_json != null && type_json.get_status()) {
							HashMap< String, HashMap<String, String> > data = type_json.get_data();
	
							Iterator<Entry<String, HashMap<String, String>>> it = data.entrySet().iterator();
							Log.i("typename start", "");
							while(it.hasNext()){
								java.util.Map.Entry<String, HashMap<String, String>> entry = (java.util.Map.Entry<String, HashMap<String, String>>)it.next();
								
								HashMap<String, String> tmp_map = (HashMap<String, String>)entry.getValue();
								if (tmp_map.get("typename").equals(type_name)) {
									typeid = tmp_map.get("typeid");
									break;
								}
							}
						} else {
							typeid = "0";
						}
						Log.i("typeid", typeid + "");
						
						List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
						params.add(new BasicNameValuePair("fid", fid));
						params.add(new BasicNameValuePair("subject", subject.getText().toString()));
						params.add(new BasicNameValuePair("typeid", typeid));
						params.add(new BasicNameValuePair("message", message.getText().toString()));
						
						
						Json_Data result = Submit.submit(user, params, Url_Config.post_newthread, null);
						if (null == result) {
							Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
						} else if (!result.get_status()) {
							Toast.makeText(getApplicationContext(), result.get_info(), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), result.get_info(), Toast.LENGTH_SHORT).show();
							finish();
						}
						
						sure.setText("确认");
						sure.setClickable(true);
						
					}
					
				}, 1000);
			}
			
		});
	}
}