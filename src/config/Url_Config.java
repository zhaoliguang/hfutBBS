package config;

public class Url_Config {
	public static String base_url = "http://bbs.hfut.edu.cn/";
	public static String login_url = base_url + "api/index.php?c=applogin&a=login";
	public static String get_forum = base_url + "api/index.php?c=appget&a=get_forum";
	public static String get_threads = base_url + "api/index.php?c=appget&a=get_threads";
	public static String get_parent_forum = base_url + "api/index.php?c=appget&a=get_parent_forum";
	public static String get_thread_content = base_url + "api/index.php?c=appget&a=get_thread_content";
	public static String get_thread_replies = base_url + "api/index.php?c=appget&a=get_thread_replies";
	public static String post_newreply = base_url + "api/index.php?c=apppost&a=post&action=newreply";
	public static String post_newthread = base_url + "api/index.php?c=apppost&a=post&action=newthread";
	public static String get_type = base_url + "api/index.php?c=appget&a=get_type";
	public static String update_url = base_url + "api/index.php?c=appupdate&a=get_update";
	public static String get_reply_info = base_url + "api/index.php?c=appget&a=get_reply_info";
	public static String get_children_forum = base_url + "api/index.php?c=appget&a=get_children_forum";
	public static String get_replies = base_url + "api/index.php?c=appget&a=get_replies";
}