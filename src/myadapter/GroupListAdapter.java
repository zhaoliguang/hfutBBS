package myadapter;
import java.util.HashMap;
import java.util.List;

import cn.edu.hfut.bbs.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupListAdapter extends BaseExpandableListAdapter {
	private Context  context;
	private Tree     tree;
	
	private int      parentLayoutId;
	private String[] parentTagName;
	private int[]    parentTagId;
	
	private int      childLayoutId;
	private String[] childTagName;
	private int[]    childTagId;
	
	private boolean[] isOpen;
	
	public GroupListAdapter(Context context, Tree tree, int parentLayoutId, String[] parentTagName, 
							int[] parentTagId, int childLayoutId, String[] childTagName, int[] childTagId) {
		this.context        = context;
		this.tree           = tree;
		this.parentLayoutId = parentLayoutId;
		this.parentTagName  = parentTagName;
		this.parentTagId    = parentTagId;
		this.childLayoutId  = childLayoutId;
		this.childTagName   = childTagName;
		this.childTagId     = childTagId;
		
		isOpen              = new boolean[tree.getRoot().getChildren().size()]; 
	}
	


	@Override
	public Object getChild(int groupPosition, int childPosition) {
		
		TreeNode root = tree.getRoot();
		List<TreeNode> parentChildren = root.getChildren();
		TreeNode parentNode = parentChildren.get(groupPosition);
		List<TreeNode> children = parentNode.getChildren();
		Object child = children.get(childPosition);
		
		return child;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		TreeNode child = (TreeNode)this.getChild(groupPosition, childPosition);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View childView = inflater.inflate(childLayoutId, null);
		HashMap<Object, Object> property = child.getProperty();
		
		int maxLength = Math.max(childTagName.length, childTagId.length);
		for (int i = 0; i < maxLength ; i++) {
			View view = childView.findViewById(childTagId[i]);
			TextView t = (TextView)view;
			t.setText((String)property.get(childTagName[i]));
		}
		
		return childView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		TreeNode root = tree.getRoot();
		return root.getChildren().get(groupPosition).getChildren().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		TreeNode root = tree.getRoot();
		return root.getChildren().get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		TreeNode root = tree.getRoot();
		return root.getChildren().size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		
		
		TreeNode parentNode = tree.getRoot().getChildren().get(groupPosition);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(parentLayoutId, null);
		
		HashMap<Object, Object> property = parentNode.getProperty();
		
		int maxLength = Math.max(parentTagName.length, parentTagId.length);
		
		for (int i = 0; i < maxLength ; i++) {
			
			View view = layout.findViewById(parentTagId[i]);

			if (view.getClass().getName().equals("android.widget.TextView")) {
				((TextView)view).setText((String)property.get(parentTagName[i]));
			}
			
		}
		
		ImageView r = (ImageView)layout.findViewById(R.id.imageView1);
		
		if (isOpen[groupPosition]) {
			r.setImageResource(R.drawable.forum_activity_1_2);
		} else {
			r.setImageResource(R.drawable.forum_activity_1_3);
		}
		return layout;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		// return false;
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		// return false;
		return true;
	}
	
	public void setOpen(int groupPosition, boolean flag) {
		isOpen[groupPosition] = flag;
	}

}