package service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class HttpClientService extends Service {

    private static final String TAG = "LocalService"; 
    private IBinder binder = new HttpClientService.LocalBinder();
    
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    MediaPlayer mediaPlayer=null;
    @Override 
    public void onCreate() { 
    	Log.i(TAG, "onCreate"); 
    	//这里可以启动媒体播放器
    	// if(mediaPlayer==null)
    	//     mediaPlayer=MediaPlayer.create(this, uri);
    	super.onCreate(); 
    } 

    @Override 
    public void onStart(Intent intent, int startId) { 
    	Log.i(TAG, "onStart"); 
    	super.onStart(intent, startId); 
    } 

    @Override 
    public int onStartCommand(Intent intent, int flags, int startId) { 
    	Log.i(TAG, "onStartCommand"); 
        return START_STICKY;
    }

    
    
    @Override 
    public void onDestroy() {
    	Log.i(TAG, "onDestroy"); 
    	super.onDestroy(); 
    } 

    
    //定义内容类继承Binder
    public class LocalBinder extends Binder{
        //返回本地服务
    	HttpClientService getService(){
            return HttpClientService.this;
        }
    }
    
    
}