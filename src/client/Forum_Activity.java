package client;



import tools.*;
import android.app.*;
import android.content.*;
 

import java.util.*;
import java.util.Map.Entry;

import myadapter.GroupListAdapter;
import myadapter.Tree;
import myadapter.TreeNode;

import org.apache.http.message.BasicNameValuePair;


import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
 
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.AdapterView.*;

import cn.edu.hfut.bbs.R;

import config.Url_Config;

public class Forum_Activity extends Activity {
	private User user;
	private long mExitTime;
	private Tree listItem;
	private ExpandableListView list;
	private GroupListAdapter listItemAdapter;
	
	
	public Forum_Activity() {
		user = null;
		mExitTime = 0;
		listItem = new Tree();
		list = null;
		listItemAdapter = null;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum_activity_layout_1_1);
		
		/// 初始化变量
		user = (User)getIntent().getSerializableExtra("user");
		list = (ExpandableListView)findViewById(R.id.forum_activity_1_2_flipper);
		init();
		
		Button m = (Button)findViewById(R.id.forum_activity_1_1_btn);
		m.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(Forum_Activity.this, "未实现", Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
	private void init() {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				loadData();
				
				String[] childTagName = new String[] {"name", "todaypost", "forum_zhuti", "forum_posts"};
				int[]    childTagId   = new int[] {R.id.forum_item_1_1_name, R.id.forum_item_1_2_todaypost, R.id.forum_item_1_4_replies_author, R.id.forum_item_1_5_forum_posts};
				
				String[] parentTagName = new String[] {"name"};
				int[]    parentTagId   = new int[] {R.id.forum_item_parent_1_1_group_name};
		        listItemAdapter = new GroupListAdapter (
		        		Forum_Activity.this,		// Context
		        		listItem,   				// Tree
		        		R.layout.forum_activity_layoug_1_3,	// parentLayoutId
		        		parentTagName,		    	// parentTagName
		        		parentTagId,				// parentTagId
		        		R.layout.forum_item,		// childLayoutId
		        		childTagName,	     		// childTagName
		        		childTagId  				// childTagId
		        );
		        list.setGroupIndicator(null);
		        list.setAdapter(listItemAdapter);
		        list.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						listItemAdapter.setOpen(groupPosition, false);
					}
		        	
		        });
		        
		        list.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						listItemAdapter.setOpen(groupPosition, true);
						
					}
		        	
		        });
		        
		        list.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition, long id) {
						HashMap<Object, Object> o = listItem.getRoot().getChildren().get(groupPosition).getChildren().get(childPosition).getProperty();
						String fid = (String)o.get("fid");
						
						Intent forum_intent = new Intent();
						forum_intent.setClass(Forum_Activity.this, ViewThreads_Activity.class);
						Bundle data_bundle = new Bundle();
						data_bundle.putSerializable("user", user);
						data_bundle.putString("fid", fid);

						
						forum_intent.putExtras(data_bundle);
						startActivity(forum_intent);
						return false;
					}
		        	
		        });

			}
			
		});
	}
	
	

	
	
	private void loadData() {
		// 获得父版块
		Json_Data more_forums = Submit.submit(user, null, Url_Config.get_parent_forum, "fid");
		
		if (more_forums == null) {
			Toast.makeText(getApplicationContext(), "获取板块失败", Toast.LENGTH_SHORT).show();
			return;
		} else if (!more_forums.get_status()) {
			Toast.makeText(getApplicationContext(), more_forums.get_info(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		HashMap< String, HashMap<String, String> > data = more_forums.get_data();
				
		/// 排序
		List<Entry< String, HashMap<String, String> >> sorted_data = new ArrayList<Entry< String, HashMap<String, String> >>(data.entrySet());

		Collections.sort(sorted_data, new Comparator<Entry< String, HashMap<String, String> >>() {

			@Override
			public int compare(Entry<String, HashMap<String, String>> lhs,
					Entry<String, HashMap<String, String>> rhs) {
				return (Integer.parseInt(rhs.getValue().get("fid").toString()) - 
						Integer.parseInt(lhs.getValue().get("fid").toString()));
			}

		}); 
		
		TreeNode root = new TreeNode();
		
		for (int i = 0; i < sorted_data.size(); i++) {
			
			HashMap<String, String> tmp_map = sorted_data.get(i).getValue();
			HashMap<Object, Object> parent_property = new HashMap<Object, Object>();
			parent_property.put("name", Tools.filterForumParent(tmp_map.get("name")));
			
			
			List<Entry< String, HashMap<String, String> >> children = getChildList(Integer.valueOf(tmp_map.get("fid")));
			List<TreeNode> childList = new ArrayList<TreeNode>();
			if (null == children) {
				Toast.makeText(getApplicationContext(), "获取板块失败", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			
			for (int j = 0; j < children.size(); j++) {
				HashMap<String, String> child_tmp_map = children.get(j).getValue();
				HashMap<Object, Object> result = new HashMap<Object, Object>();
				
				result.put("name", child_tmp_map.get("name"));
				result.put("todaypost", child_tmp_map.get("todayposts"));
				result.put("forum_zhuti", child_tmp_map.get("threads"));
				result.put("forum_posts", child_tmp_map.get("posts"));
				result.put("fid", child_tmp_map.get("fid"));
				childList.add(new TreeNode(child_tmp_map.get("name"), null, result));
			}
			
			TreeNode new_node = new TreeNode();
			new_node.setNode(tmp_map.get("name"));
			new_node.setProperty(parent_property);
			new_node.setChildren(childList);
			root.addChild(new_node);
		}
		listItem.setRoot(root);
		
	}
	
	private List<Entry< String, HashMap<String, String> >> getChildList(int parentFid) {
		// 获得子版块
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("fup", String.valueOf(parentFid)));
		Json_Data more_forums = Submit.submit(user, params, Url_Config.get_children_forum, "fid");
				
		if (more_forums == null) {
			Toast.makeText(getApplicationContext(), "获取板块失败", Toast.LENGTH_SHORT).show();
			return null;
		} else if (!more_forums.get_status()) {
				Toast.makeText(getApplicationContext(), more_forums.get_info(), Toast.LENGTH_SHORT).show();
			return null;
		}
				
		HashMap< String, HashMap<String, String> > data = more_forums.get_data();
						
		/// 排序
		List<Entry< String, HashMap<String, String> >> sorted_data = new ArrayList<Entry< String, HashMap<String, String> >>(data.entrySet());

		Collections.sort(sorted_data, new Comparator<Entry< String, HashMap<String, String> >>() {

			@Override
			public int compare(Entry<String, HashMap<String, String>> lhs,
				Entry<String, HashMap<String, String>> rhs) {
				return (Integer.parseInt(rhs.getValue().get("fid").toString()) - 
									Integer.parseInt(lhs.getValue().get("fid").toString()));
			}   
					
		}); 
				
		return sorted_data;
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
			String fid = map.get("fid");
			
		    Intent forum_intent = new Intent();
			forum_intent.setClass(Forum_Activity.this, ViewThreads_Activity.class);
			Bundle data_bundle = new Bundle();
			data_bundle.putSerializable("user", user);
			data_bundle.putString("fid", fid);

			
			forum_intent.putExtras(data_bundle);
			startActivity(forum_intent);
		}  
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        mExitTime = System.currentTimeMillis();
                } else {
                        finish();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
	}
}