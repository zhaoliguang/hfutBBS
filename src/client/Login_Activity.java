package client;

import tools.*;
import cn.edu.hfut.bbs.R;

import config.Url_Config;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.os.*;

public class Login_Activity extends Activity {
	private EditText edittext_username;	// 用户名
	private EditText edittext_password;	// 密码
	private EditText edittext_answer;	// 答案
	private Button btn_login;			// 登陆
	private Spinner question_spinner;	// 问题
	private ArrayAdapter<?> question_adapter;	// 问题选项
	private long mExitTime;
	private CheckBox remember;			// 记住密码
	private String user_file_path;		// 记住密码文件路径
	private User old_user;				// 记住密码用户
	
	/**
	 * 
	 * 构造
	 */
	public Login_Activity() {
		edittext_username = null;
		edittext_password = null;
		edittext_answer = null;
		btn_login = null;
		question_spinner = null;
		question_adapter = null;
		mExitTime = 0;
		remember = null;
		user_file_path = null;


		
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout_1_1);
		
		/// 检查更新
		Update update = new Update(Login_Activity.this);		
		update.checkUpdate();
		
		/// 获取id
		edittext_username = (EditText)findViewById(R.id.login_edittext_username);
		edittext_password = (EditText)findViewById(R.id.login_edittext_passwordc);
		edittext_answer = (EditText)findViewById(R.id.login_edittext_answer);
		btn_login = (Button)findViewById(R.id.login_btn_login);
		remember = (CheckBox)findViewById(R.id.login_remember);

		/// 问题spinner美化
		question_spinner = (Spinner)findViewById(R.id.login_spinner_question);

		question_adapter = ArrayAdapter.createFromResource(this, R.array.login_question, android.R.layout.simple_spinner_item);
		question_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		question_spinner.setAdapter(question_adapter);
		question_spinner.setVisibility(View.VISIBLE);
		
		/// 登陆按钮事件
		btn_login.setOnClickListener(new btn_login_listener());
		
		/// 历史记录
		user_file_path = Login_Activity.this.getFilesDir().getAbsolutePath() + "/ef0e567509eb923dfc6296bd8802acd9";
		old_user = Tools.readFromFile(user_file_path);
		if (old_user != null) {
			remember.setChecked(true);
			
			edittext_username.setText(old_user.get_username());
			edittext_password.setText(old_user.get_password());
			question_spinner.setSelection(Integer.parseInt(old_user.get_questionid()));
			edittext_answer.setText(old_user.get_answer());
		}
		
		
	}
	
	class btn_login_listener implements OnClickListener {
		private Json_Data json;
		
		public btn_login_listener() {
			json = null;
		}
		
		private boolean login() {
			/// 获得变量
			String username = edittext_username.getText().toString();
			String password = edittext_password.getText().toString();
			String answer = edittext_answer.getText().toString();
			
			if (username == null || username.equals("") || password == null || password.equals("")) {
				Toast.makeText(getApplicationContext(), "请填写完整", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			/// 问题id
			String question = question_spinner.getSelectedItem().toString();
			String questionid;
			if (question.equals("母亲的名字")) {
				questionid = "1";
			} else if (question.equals("爷爷的名字")) {
				questionid = "2";
			} else if (question.equals("父亲出生的城市")) {
				questionid = "3";
			} else if (question.equals("您其中一位老师的名字")) {
				questionid = "4";
			} else if (question.equals("您个人计算机的型号")) {
				questionid = "5";
			} else if (question.equals("您最喜欢的餐馆名称")) {
				questionid = "6";
			} else if (question.equals("驾驶执照的最后四位数字")) {
				questionid = "1";
			} else {
				questionid = "0";
			}
			
			if (!questionid.equals("0") && answer.equals("")) {
				Toast.makeText(getApplicationContext(), "请输入答案", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			User user = new User(username, password, questionid, answer);
			json = Submit.submit(user, null, Url_Config.login_url, null);
			
			if (json == null) {
				Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
				return false;
			} else if (!json.get_status()) {
				Toast.makeText(getApplicationContext(), json.get_info(), Toast.LENGTH_SHORT).show();
				return false;
			} else {
				Intent forum_intent = new Intent();
				forum_intent.setClass(Login_Activity.this, Forum_Activity.class);
				Bundle data_bundle = new Bundle();
				data_bundle.putSerializable("user", user);
				forum_intent.putExtras(data_bundle);
				startActivity(forum_intent);
				
			}
			if (remember.isChecked()) {
				Tools.saveToFile(user_file_path, user);
			} else {
				Tools.deleteFile(user_file_path);
			}
			finish();
			return true;
		}
		
		@Override
		public void onClick(View arg0) {
			/// 改变样式
			final Button btn = (Button)arg0;
			btn.setText("登陆中");
			btn.setClickable(false);
			
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					try {
						if (!login()) {
							btn.setText("登陆");
							btn.setClickable(true);
						}
					} catch (Exception e) {
						btn.setText("登陆");
						btn.setClickable(true);
					}
				}
			}, 2000);
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