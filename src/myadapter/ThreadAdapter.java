package myadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import client.Reply_Activity;
import client.User;
import cn.edu.hfut.bbs.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThreadAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> data;
	private Context context;
	private LayoutInflater inflater;
	private User user;
	
	public ThreadAdapter(Context context, ArrayList<HashMap<String, Object>> listItem, User user) {
		this.context = context;
		this.data = listItem;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.user = user;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		View view = inflater.inflate(R.layout.thread_activity_1_2, null);
		LinearLayout layout = (LinearLayout)view.findViewById(R.id.LinearLayout1);
		TextView author = (TextView)layout.findViewById(R.id.viewthread_1_1);
		TextView time   = (TextView)layout.findViewById(R.id.viewthread_1_2);
		TextView ceng   = (TextView)layout.findViewById(R.id.viewthread_1_3);
		TextView content = (TextView)layout.findViewById(R.id.viewthread_1_4);
		Button reply = (Button)view.findViewById(R.id.forum_activity_1_1_btn);
		Button quote = (Button)view.findViewById(R.id.viewthread_1_6);
		
		HashMap<String, Object> tmp = data.get(position);
		if (tmp != null && tmp.size() > 0) {
			author.setText((String)tmp.get("author"));
			time.setText((String)tmp.get("time"));
			ceng.setText(String.valueOf(position + 1));
			content.setText((String)tmp.get("message"));
			
			reply.setOnClickListener(new ReplyAndQuoteListener((String)tmp.get("fid"), (String)tmp.get("tid"), (String)tmp.get("pid"), user, "reply"));
			quote.setOnClickListener(new ReplyAndQuoteListener((String)tmp.get("fid"), (String)tmp.get("tid"), (String)tmp.get("pid"), user, "quote"));
		}
		
		return view;
	}
	
	private class ReplyAndQuoteListener implements OnClickListener {
		private String fid;
		private String tid;
		private String pid;
		private User user;
		private String type;
		
		public ReplyAndQuoteListener(String fid, String tid, String pid, User user, String type) {
			this.fid = fid;
			this.tid = tid;
			this.pid = pid;
			this.user = user;
			this.type = type;
		}
		
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
	    	Bundle bundle = new Bundle();

	    	bundle.putString("fid", fid);
	    	bundle.putString("tid", tid);
	    	bundle.putString("pid", pid);
	    	bundle.putString("type", type);
	    	bundle.putSerializable("user", user);

	    	intent.setClass(context, Reply_Activity.class);
	    	intent.putExtras(bundle);
	    	context.startActivity(intent);
		}
		
	}
	
}