package client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Forum implements Serializable{

	private String fid;
	private String name;
	
	public Forum(String fid, String name) {
		this.fid = fid;
		this.name = name;
	}
	
	public String get_fid() {
		return fid;
	}
	
	public String get_name() {
		return name;
	}
}