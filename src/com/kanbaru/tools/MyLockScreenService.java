package com.kanbaru.tools;


import com.kanbaru.DIYview.MyImgView;
import com.kanbaru.DIYview.MyImgViewGroup;
import com.kanbaru.digitalunlock.CheckPointActivity;
import com.kanbaru.digitalunlock.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class MyLockScreenService extends Service{
	public static final String LOCK_ACTION = "lock";
	public static final String UNLOCK_ACTION = "unlock";
	public static SharedPreferences SP;
	private MyImgViewGroup mImgViewGroup;
	boolean IsDefault;
	private Context mContext;
	public ViewGroup mViewGroup;
	private WindowManager mWindowManager;
	private int x,y;
	private int password_Num;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		 Notification notification = new Notification(R.drawable.ic_launcher,  
				 getString(R.string.app_name), System.currentTimeMillis());  
				  
				 PendingIntent pendingintent = PendingIntent.getActivity(this, 0,  
				 new Intent(this, CheckPointActivity.class), 0);  
				 notification.setLatestEventInfo(this, "uploadservice", "请保持程序在后台运行",  
				 pendingintent);  
		mContext = getApplicationContext();
		
		mWindowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
		SP=this.getSharedPreferences("data", Context.MODE_PRIVATE);
		IsDefault = SP.getBoolean("default", true);
		password_Num=SP.getInt("PW_Num", 0);
		x=SP.getInt("widget_center_x", 0);
		y=SP.getInt("widget_center_y", 0);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent.getAction();
		flags = Service.START_STICKY;  
	    return super.onStartCommand(intent, flags, startId);
	}
	
}
