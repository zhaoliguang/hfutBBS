package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;

import tools.Json_Data;
import tools.Submit;
import tools.Tools;

import cn.edu.hfut.bbs.R;

import config.Url_Config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewThreads_Activity extends Activity {
	private User user;
	private View loadMoreView;
	private Button loadMoreButton;
	private ListView list;
	private SimpleAdapter listItemAdapter;
	private int page;
	private ArrayList<HashMap<String, Object>> listItem;
	private String fid;
	private Handler handler;
	private Button fatie;
	
	public ViewThreads_Activity() {		
		user = null;
		loadMoreView = null;
		loadMoreButton = null;
		list = null;
		listItemAdapter = null;
		page = 0;
		listItem = new ArrayList<HashMap<String, Object>>();
		fid = null;
		handler = new Handler();
		fatie = null;
	}
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewthread_activity_layout_1_1);
		
		/// 初始化控件
		list = (ListView)findViewById(R.id.threads_list);
		user = (User)getIntent().getSerializableExtra("user");
		fid = getIntent().getExtras().getString("fid");
		fatie = (Button)findViewById(R.id.viewthread_activity_1_1_btn);
		
		fatie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ViewThreads_Activity.this, Fatie_Activity.class);
				Bundle data_bundle = new Bundle();
				data_bundle.putSerializable("user", user);
				data_bundle.putString("fid", fid);
				
				intent.putExtras(data_bundle);
				startActivity(intent);
			}
			
		});
		
		/// 加载更多控件
		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
		loadMoreButton = (Button)loadMoreView.findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loadMore();				
			}
			
		});
		
		list = (ListView)findViewById(R.id.threads_list);
		list.addFooterView(loadMoreView);

		
		init();
	}
	
	private void init() {
		handler.post(new Runnable() {
			
			@Override
			public void run() {				
				
				// 生成适配器的Item和动态数组对应的元素  
		        listItemAdapter = new SimpleAdapter(
		        		ViewThreads_Activity.this,
		        		listItem,   
		        		R.layout.viewthread_activity_layout_1_2,          
		        		new String[] {"subject", "item_replies", "author", "time"},     
		        		new int[] {R.id.subject, R.id.item_replies, R.id.author, R.id.time}  
		        );
		        
		        list.setAdapter(listItemAdapter);
		        
		        list.setOnItemClickListener(new ListItem_Listener(user));
		        
		        loadMore();
			}
		});
	}
	
	class ListItem_Listener implements OnItemClickListener {
		private User user;
		
		public ListItem_Listener(User user) {
			this.user = user;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  
			ListView listView = (ListView)parent;  
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);  
		    String tid = map.get("tid");
		    String fid = map.get("fid");
		    String subject = map.get("subject");
		    
		    
			
		    Intent forum_intent = new Intent();
			forum_intent.setClass(ViewThreads_Activity.this, Thread_Activity.class);
			Bundle data_bundle = new Bundle();
			data_bundle.putSerializable("user", user);
			data_bundle.putString("tid", tid);
			data_bundle.putString("fid", fid);
			data_bundle.putString("subject", subject);
			
			forum_intent.putExtras(data_bundle);
			startActivity(forum_intent);
		}  
	}


	public void loadMore() {
		loadMoreButton.setText("加载中");
		loadMoreButton.setClickable(false);
		handler.postDelayed(new Runnable() {
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
		params.add(new BasicNameValuePair("page", String.valueOf(page)));
		
		Json_Data more_threads = Submit.submit(user, params, Url_Config.get_threads, "tid"); 
		if (more_threads == null) {
			Toast.makeText(getApplicationContext(), "获取帖子失败", Toast.LENGTH_SHORT).show();
			page -= 1;
			return;
		} else if (!more_threads.get_status()) {
			Toast.makeText(getApplicationContext(), more_threads.get_info(), Toast.LENGTH_SHORT).show();
			page -= 1;
			return;
		}
		
		HashMap< String, HashMap<String, String> > data = more_threads.get_data();
		
		/// 排序
		List<Entry< String, HashMap<String, String> >> sorted_data = new ArrayList<Entry< String, HashMap<String, String> >>(data.entrySet());
		Collections.sort(sorted_data, new Comparator<Entry< String, HashMap<String, String> >>() {

			@Override
			public int compare(Entry<String, HashMap<String, String>> lhs,
					Entry<String, HashMap<String, String>> rhs) {
				return (rhs.getKey().toString().compareTo(lhs.getKey().toString()));
			}   
				
		}); 

				
		for (int i = 0; i < sorted_data.size(); i++) {
			HashMap<String, String> tmp_map = sorted_data.get(i).getValue();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("subject", tmp_map.get("subject"));
			map.put("author", tmp_map.get("author"));
			map.put("time", Tools.unix2time(tmp_map.get("dateline")));
			map.put("item_replies", Html.fromHtml(tmp_map.get("replies")));
			map.put("tid", tmp_map.get("tid"));
			map.put("fid", tmp_map.get("fid"));
			listItem.add(map);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
	}
	

}