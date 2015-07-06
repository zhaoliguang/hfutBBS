package myadapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// �ڵ���
public class TreeNode {
	private Object node;
	private List<TreeNode> children;		// �ӷ�֧
	private HashMap<Object, Object> property;	// ��ǰ�ڵ����Լ���
	
	public TreeNode() {
		node = null;
		children = new ArrayList<TreeNode>();
		property = new HashMap<Object, Object>();
	}
	
	public TreeNode(Object node, List<TreeNode> children, HashMap<Object, Object> property) {
		this.node     = node;
		this.children = children;
		this.property = property;
	}
	
	/// GET AND SET OPERATION
	public Object getNode() {
		return node;
	}
	
	public void setNode(Object node) {
		this.node = node;  
	}
	
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
	public List<TreeNode> getChildren() {
		return children;
	}
	
	public HashMap<Object, Object> getProperty() {
		return property;
	}
	
	public void setProperty(HashMap<Object, Object> property) {
		this.property = property;
	}
	
	/// PUBLIC OPERATION
	public void addChild(TreeNode child) {
		if (null == children) {
			children = new ArrayList<TreeNode>();
		}
		children.add(child);
	}
	
	
}