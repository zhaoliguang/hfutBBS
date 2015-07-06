package client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String username;
	private String password;
	private String questionid;
	private String answer;
	
	public User(String username, String password, String questionid, String answer) {
		this.username = username;
		this.password = password;
		this.questionid = questionid;
		this.answer = answer;
	}
	
	public String get_username() {
		return username;
	}
	
	public String get_password() {
		return password;
	}
	
	public String get_questionid() {
		return questionid;
	}
	
	public String get_answer() {
		return answer;
	}
	

}