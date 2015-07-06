package tools;


import java.util.LinkedList;
import java.util.List;


import org.apache.http.HttpResponse;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import client.User;

public class Submit {
	public static final int timeoutConnection = 7000;
	public static final int timeoutSocket = 7000;

	public static Json_Data submit(User user, List<BasicNameValuePair> params, String url, String primary_key) {
		Json_Data result = null;
		HttpPost postMethod = null;
		DefaultHttpClient client = null;
		HttpResponse response = null;
		
		
		HttpParams httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.  
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);  
	    client = new DefaultHttpClient(httpParameters);    
		
		
		if (params == null) {
			params = new LinkedList<BasicNameValuePair>();
		}
		if (null != user) {
			String username = user.get_username();
			String password = user.get_password();
			String questionid = user.get_questionid();
			String answer = user.get_answer();
			
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("questionid", questionid));
			params.add(new BasicNameValuePair("answer", answer));
		}
		
		try {
			postMethod = new HttpPost(url);
			postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			response = client.execute(postMethod);
			String json = EntityUtils.toString(response.getEntity(), "utf-8");
			Log.i("result", json);
			result = new Json_Data(json, primary_key);
			
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
}


