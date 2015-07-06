package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import myadapter.ThreadAdapter;

import org.apache.http.message.BasicNameValuePair;

import tools.ContentFilter;
import tools.Json_Data;
import tools.Submit;


import cn.edu.hfut.bbs.R;

import config.Url_Config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.View.OnClickListener;

import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Thread_Activity extends Activity {
	private User user;
	private String fid;
	private String tid;
	private int page;
	private View loadMoreView;
	private Button loadMoreButton;
	private ListView list;
	private ThreadAdapter listItemAdapter;
	private ArrayList<HashMap<String, Object>> listItem;
	private String subject;
	private TextView t_subject;
	

	
	public Thread_Activity() {
		user = null;
		fid = null;
		tid = null;
		page = 0;
		loadMoreView = null;
		loadMoreButton = null;
		list = null;
		listItemAdapter = null;
		listItem = new ArrayList<HashMap<String, Object>>();
		subject = null;
	}
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thread_activity_1_1);
		
		t_subject = (TextView)findViewById(R.id.thread_activity_1_1);
		
		/// 加载更多控件
		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
		loadMoreButton = (Button)loadMoreView.findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				loadMore();				
			}
					
		});
		
		list = (ListView)findViewById(R.id.thread_1_2);
		list.addFooterView(loadMoreView);
		
		
		registerForContextMenu(list);
		
		
		init();
	}
	
	/**
	 * 获得更多回复
	 */
	private void loadMore() {
		loadMoreButton.setText("加载中");
		loadMoreButton.setClickable(false);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				loadData();
				
				listItemAdapter.notifyDataSetChanged();

				loadMoreButton.setText(getResources().getString(R.string.load_more));
				loadMoreButton.setClickable(true);
			}
		}, 2000);
	}
	
	private void loadData() {
		page += 1;
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("fid", fid));
		params.add(new BasicNameValuePair("tid", tid));
		params.add(new BasicNameValuePair("page", String.valueOf(page)));
		
		
		Json_Data more_replies = Submit.submit(user, params, Url_Config.get_replies, "pid"); 
		if (more_replies == null) {
			Toast.makeText(getApplicationContext(), "获取回复失败", Toast.LENGTH_SHORT).show();
			page -= 1;
			return;
		} else if (!more_replies.get_status()) {
			Toast.makeText(getApplicationContext(), more_replies.get_info(), Toast.LENGTH_SHORT).show();
			page -= 1;
			return;
		}
		
		HashMap< String, HashMap<String, String> > data = more_replies.get_data();
		
		/// 排序
		List<Entry< String, HashMap<String, String> >> sorted_data = new ArrayList<Entry< String, HashMap<String, String> >>(data.entrySet());
		Collections.sort(sorted_data, new Comparator<Entry< String, HashMap<String, String> >>() {

			@Override
			public int compare(Entry<String, HashMap<String, String>> lhs,
					Entry<String, HashMap<String, String>> rhs) {
				return (lhs.getKey().toString().compareTo(rhs.getKey()));
			}   
		
		}); 

		
		for (int i = 0; i < sorted_data.size(); i++) {
			HashMap<String, String> tmp_map = sorted_data.get(i).getValue();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("author", tmp_map.get("author"));
			map.put("fid", tmp_map.get("fid"));
			map.put("tid", tmp_map.get("tid"));
			map.put("pid", tmp_map.get("pid"));
			map.put("message", ContentFilter.doing(tmp_map.get("message")));
			listItem.add(map);
		}
		
	}
	
	private void init() {
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				list = (ListView)findViewById(R.id.thread_1_2);
				
				Intent intent = getIntent();
				Bundle bundle = intent.getExtras();
				/// 获得传递变量
				user = (User)intent.getSerializableExtra("user");
				fid = bundle.getString("fid");
				tid = bundle.getString("tid");
				subject = bundle.getString("subject");
				t_subject.setText(subject);
				listItemAdapter = new ThreadAdapter(
		        		Thread_Activity.this,
		        		listItem,
		        		user  
		        );
		        
		        list.setAdapter(listItemAdapter);
		        loadMore();
			}
		});
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
	}
}