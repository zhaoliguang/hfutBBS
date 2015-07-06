package tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

@SuppressWarnings("serial")
public class Json_Data implements Serializable {
	private String info;
	private boolean status;
	private HashMap< String, HashMap<String, String> > data;
	
	public boolean get_status() {
		return status;
	}
	
	public String get_info() {
		return info;
	}
	
	public Json_Data() {
		info = null;
		status = false;
		data = null;
	}
	
	public Json_Data(String json, String primary_key) {
		info = null;
		status = false;
		data = null;
		set(json, primary_key);
	}
	
	public HashMap< String, HashMap<String, String> > get_data() {
		return data;
	}
	
	public Json_Data(String info, HashMap< String, HashMap<String, String> > data, boolean status) {
		info = null;
		status = false;
		data = null;
		set(info, data, status);
	}
	
	public boolean set(String json, String primary_key) {
		if (data != null) {
			data.clear();
		}
		info = null;
		status = false;
		data = null;
		
		boolean result = false;
		try {
			data = new HashMap< String, HashMap<String, String> >();
		    JSONTokener jsonParser = new JSONTokener(json);
		    JSONObject object = (JSONObject) jsonParser.nextValue();
		    
		    String tmp_info = object.getString("info");
		    JSONArray tmp_data = object.getJSONArray("data");
		    boolean tmp_status = object.getInt("status") == 1 ? true : false;
		    
		    this.info = tmp_info;
	    	this.status = tmp_status;
	    	
		    if (tmp_status) {
		    	
			    for (int i = 0; i < tmp_data.length(); ++i) {
			    	JSONObject tmp_object = (JSONObject)tmp_data.get(i);
			    	Iterator<?> it = tmp_object.keys();
			    	HashMap<String, String> tmp_hashmap = new HashMap<String, String>();
		            while (it.hasNext()) {
		                String tmp_name = (String)it.next();
		                String tmp_value = tmp_object.getString(tmp_name);
		                tmp_hashmap.put(tmp_name, tmp_value);
		            }
		            
		            this.data.put(tmp_hashmap.get(primary_key), tmp_hashmap);
			    }
		    }
		    result = true;
		} catch (JSONException ex) {
			ex.printStackTrace();
			if (data != null) {
				data.clear();
			}
			data = null;
			result = false;
		}
		return result;
	}
	
	public void set(String info, HashMap< String, HashMap<String, String> > data, boolean status) {
		if (data != null) {
			data.clear();
		}
		this.info = info;
		this.data = data;
		this.status = status;
	}
	
	public HashMap<String, String> getValue(String name) {
		return data.get(name);
	}
}