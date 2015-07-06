package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;

import client.User;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


public class Tools {
	public static List<BasicNameValuePair> Map2List(HashMap<String, String> data) {
		List<BasicNameValuePair> result = new LinkedList<BasicNameValuePair>();
		
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		
		while(it.hasNext()){
			java.util.Map.Entry<String, String> entry = (java.util.Map.Entry<String, String>)it.next();
			result.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
		}
		
		return result;
	}
	
	public static String unix2time(String unix_time) {
		String result = null;
		long now = new Date().getTime();
		long dst = Long.parseLong(unix_time);
		
		long sub = now - dst;
		if (sub <= 12 * 60 * 60) {	// 时
			if (sub <= 60 * 60) {	// 分
				if (sub <= 60) {	// 秒
					result = String.valueOf(sub) + "秒前";
				} else {
					result = String.valueOf(sub % 60) + "分钟前";
				}
			} else {
				result = String.valueOf(Math.floor(sub / 60 / 60)) + "小时前";
			}
		} else {
			result = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new java.util.Date(dst * 1000));
		}

		return result;
	}
	
	/** 
	 * 返回当前程序版本名 
	 */  
	public static String getAppVersionName(Context context) {  
	    String versionName = "";  
	    try {  
	        // ---get the package info---  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionName = pi.versionName;
	        if (versionName == null || versionName.length() <= 0) {  
	            return "";
	        }  
	    } catch (Exception e) {  
	    }  
	    return versionName;  
	}
	
	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		 } catch (Exception e) {
		 }
	}
	
	public static void saveToFile(String path, User user) {
		FileOutputStream fos = null;  
        ObjectOutputStream oos = null;  
        File f = new File(path);  
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(user);    //括号内参数为要保存java对象  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                oos.close();  
                fos.close();  
            } catch (Exception e) {  
            }  
        }
	}
	
	public static User readFromFile(String path) {
		FileInputStream fis = null;  
        ObjectInputStream ois = null;     
        File f = new File(path);  
        User object = null;
        try {  
            fis = new FileInputStream(f);  
            ois = new ObjectInputStream(fis);  
            object = (User)ois.readObject();//强制类型转换  
        } catch (Exception e) {    
        } finally{  
            try {  
                ois.close();  
                fis.close();  
            } catch (Exception e) {  
            }  
        }
        return object;
	}
	
	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.delete();
	}
	
	public static String filterForumParent(String name) {
		String result = name;
		try {
			int i = result.indexOf(">");
			int j = result.lastIndexOf("<");
			if (i > 0 && j > 0 && i < j) {
				result = result.substring(i + 1, j);
			}
		} catch (Exception e) {
			return name;
		}
		return result;
	}
	
}