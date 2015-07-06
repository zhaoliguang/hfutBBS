package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import tools.Json_Data;
import tools.Submit;
import tools.Tools;

import config.Url_Config;
import cn.edu.hfut.bbs.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;



public class Update {
	private Json_Data update_info;
	
	private Context mContext;
	
	private final int MESSAGE_DOWN_UPDATE = 1;
	private final int MESSAGE_DOWN_OVER = 2;
	private final int MESSAGE_GET_UPDATE_INFO = 3;
	
	/// 下载安装包路径
	private String savePath;
	private String saveFileName;
	
	private ProgressBar mProgress;
	
	private Dialog noticeDialog;
	private Dialog downloadDialog;
	
	private boolean interruptFlag = false;
	
	private int progress;
	
	private Handler mHandler;
	
	private String file_url;
	
	private double old_version;
	private double new_version;
	
	

	public Update(Context context) {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MESSAGE_DOWN_UPDATE:
					mProgress.setProgress(progress);
					
					break;
				case MESSAGE_DOWN_OVER:
					installApk();
					break;
				case MESSAGE_GET_UPDATE_INFO:
					getUpdateInfo();
					break;
				default:
					break;
				}
			}
		};
		mContext = context;
	}
	
	
	private void getUpdateInfo() {
		update_info = Submit.submit(null, null, Url_Config.update_url, "versionname");
		
		if (update_info != null && update_info.get_status()) {
			HashMap<String, HashMap<String, String>> data = update_info.get_data();
			
			Iterator<Entry<String, HashMap<String, String>>> it = data.entrySet().iterator();
			
			if (it.hasNext()) {
				java.util.Map.Entry<String, HashMap<String, String>> entry = (java.util.Map.Entry<String, HashMap<String, String>>)it.next();
				HashMap<String, String> tmp_map = (HashMap<String, String>)entry.getValue();
				
				String new_versionname = tmp_map.get("versionname");
				String new_file_name = tmp_map.get("filename");
				file_url = Url_Config.base_url + tmp_map.get("fileurl");
				
				String old_versionname = Tools.getAppVersionName(mContext);
				this.old_version = Double.parseDouble(old_versionname);
				this.new_version = Double.parseDouble(new_versionname);
				
				if (this.new_version - this.old_version < 0.01) {
					return;
				}
				
				/*
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					savePath =  Environment.getExternalStorageDirectory().getAbsolutePath();
				} else {
					savePath =  Environment.getRootDirectory().getAbsolutePath();
				}
				*/
				savePath = mContext.getFilesDir().getParent() + "/tmp";
				saveFileName = savePath + "/" + new_file_name;
				
			}
			
			showNoticeDialog();
		}
		
	}
	
	/**
	 * 外部接口
	 */
	public void checkUpdate() {
		mHandler.sendEmptyMessage(MESSAGE_GET_UPDATE_INFO);
	}
	
	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setMessage("当前版本" + old_version + "最新版本" + new_version);
		
		/// 立即下载
		builder.setPositiveButton("立即下载", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
			
		});
		
		/// 取消
		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
			
		});
		
		/// 显示
		noticeDialog = builder.create();
		noticeDialog.show();
	}
	
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.progress);
		mProgress.setProgress(0);
		
		mProgress.setIndeterminate(false); 
		
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interruptFlag = true;
			}
			
		});
		downloadDialog = builder.create();
		downloadDialog.show();
		
		downloadApk();
	}
	
	/**
	 * 下载模块
	 */
	private void downloadApk() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					URL url = new URL(file_url);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.connect();
					
					int length = conn.getContentLength();
					is = conn.getInputStream();
					
					File file = new File(savePath);
					
					if (!file.exists()) {
						file.mkdirs();
						Log.i("path", file.mkdirs() + "");
					}
					
					
					File ApkFile = new File(saveFileName);
					
					if (ApkFile.exists()) {
						ApkFile.delete();
					}
					
					Tools.chmod("755", file.toString());
					Tools.chmod("755", saveFileName);
					
					
					fos = new FileOutputStream(ApkFile);
					
					int count = 0;
					byte buf[] = new byte[1024];
					int numread = is.read(buf);
					while (numread != -1 && !interruptFlag) {
						count += numread;
						progress = (int)(((float)count / length) * 100);
						
						/// 更新进度
						mHandler.sendEmptyMessage(MESSAGE_DOWN_UPDATE);
						Log.i("progress", progress + " " + mProgress.getProgress());
						
						fos.write(buf, 0, numread);
						
						numread = is.read(buf);
					} 
					
					if (progress == mProgress.getMax()) {
						mHandler.sendEmptyMessage(MESSAGE_DOWN_OVER);
					} else {
						Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
					}

					fos.close();
					is.close();
				} catch (Exception e) {
					Toast.makeText(mContext, "更新失败", Toast.LENGTH_SHORT).show();
					
				} finally {
					downloadDialog.dismiss();
				}
			}
		}).start();
	}
	
	/**
	 * 安装
	 */
	private void installApk() {
		File apkFile = new File(saveFileName);
		if (!apkFile.exists()) {
			return;
		}
		
		
		Intent i = new Intent();
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setAction(Intent.ACTION_VIEW); 
		i.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		Log.i("file", apkFile.toString());
        mContext.startActivity(i);
	}
}