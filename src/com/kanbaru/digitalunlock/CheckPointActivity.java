package com.kanbaru.digitalunlock;


import com.kanbaru.DIYview.MyImgView;
import com.kanbaru.DIYview.MyImgViewGroup;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.KeyEvent;

import android.view.View;

import android.view.Window;
import android.view.WindowManager;

import android.widget.FrameLayout;


public class CheckPointActivity extends Activity{
	private Context mContext;
	private View rootView;
	private WindowManager mWindowManager;
	
	public static SharedPreferences SP;
	private static FrameLayout FL_background_5;
	private static MyImgViewGroup mImgViewGroup;
	
	private static boolean IsDefault;
	private static boolean isSetting=false;
	public static boolean first_Tip=false;//首次进入的提示
	static Drawable d=null;
	private static int x;
	private static int y;
	private static int password_Num;
	private FrameLayout mRLayout;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_check_point);
		mContext = getApplicationContext();
		mWindowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
		SP=this.getSharedPreferences("data", Context.MODE_PRIVATE);
		mRLayout = (FrameLayout)findViewById(android.R.id.content); 
		
		WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        getWindow().setAttributes(params);
			
        isSetting=SP.getBoolean("isSetting", false);
		IsDefault=SP.getBoolean("default", true);
		
		password_Num=SP.getInt("PW_Num", 0);
		x=SP.getInt("widget_center_x", 0);
		y=SP.getInt("widget_center_y", 0);
		load_MyImgViewGroup();

		FL_background_5=(FrameLayout)findViewById(R.id.FrameLayout5);
		loadBG(FL_background_5);
		hideStatusBar();
		if(!first_Tip){
			/*
			new AlertDialog.Builder(CheckPointActivity.this).setTitle("Tip")//设置对话框标题  
			.setMessage("现在要验证一下你设置的密码")//设置显示的内容  
			.setPositiveButton("我知道了",null).show();
			*/ 
			first_Tip=true;
		}
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(isSetting){
			Editor editor=SP.edit();
			editor.putBoolean("isSetting", false);
			editor.putBoolean("islock", false);
			editor.commit();
		}
	}
	public void hideStatusBar(){//屏蔽系统通知栏
		WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
		localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
		localLayoutParams.gravity = Gravity.TOP;
		localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
		            // 布局接受触摸事件
		            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
		            // 画的时候覆盖状态栏
		            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		localLayoutParams.height = (int) (50 * getResources().getDisplayMetrics().scaledDensity);
		localLayoutParams.format = PixelFormat.TRANSPARENT;

		// 创建自定义状态栏对象，customViewGroup类需要你自己创建定义
		MyImgView view = new MyImgView(this);
		// 应用布局参数
		mWindowManager.addView(view, localLayoutParams);	
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//屏蔽返回键
		if(!isSetting){//设置时不会屏蔽back键
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onPause() {//屏蔽任务键的方法
	    super.onPause();
	    ActivityManager activityManager = (ActivityManager) getApplicationContext()
	                .getSystemService(Context.ACTIVITY_SERVICE);
	   // 如果用户点了这个键，把当前锁屏activity提到最前，不启动其他程序
	    activityManager.moveTaskToFront(getTaskId(), 0);
	}
	public static void loadBG(FrameLayout f){
		if(!IsDefault){
			d=Drawable.createFromPath(SP.getString("ImgPath",""));
			f.setBackground(d);
		}else{
			 f.setBackgroundResource(R.drawable.default_lockscreen);
		}
	}
	@SuppressWarnings("deprecation")
	void load_MyImgViewGroup(){
		mImgViewGroup=new MyImgViewGroup(this,password_Num,x,y);
		mImgViewGroup.creat(this,-100,-100);
	}
	

}
