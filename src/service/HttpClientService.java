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
    	//�����������ý�岥����
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

    
    //����������̳�Binder
    public class LocalBinder extends Binder{
        //���ر��ط���
    	HttpClientService getService(){
            return HttpClientService.this;
        }
    }
    
    
}