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
	public static boolean first_Tip=false;//�״ν������ʾ
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
			new AlertDialog.Builder(CheckPointActivity.this).setTitle("Tip")//���öԻ������  
			.setMessage("����Ҫ��֤һ�������õ�����")//������ʾ������  
			.setPositiveButton("��֪����",null).show();
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
	public void hideStatusBar(){//����ϵͳ֪ͨ��
		WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
		localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
		localLayoutParams.gravity = Gravity.TOP;
		localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
		            // ���ֽ��ܴ����¼�
		            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
		            // ����ʱ�򸲸�״̬��
		            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		localLayoutParams.height = (int) (50 * getResources().getDisplayMetrics().scaledDensity);
		localLayoutParams.format = PixelFormat.TRANSPARENT;

		// �����Զ���״̬������customViewGroup����Ҫ���Լ���������
		MyImgView view = new MyImgView(this);
		// Ӧ�ò��ֲ���
		mWindowManager.addView(view, localLayoutParams);	
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//���η��ؼ�
		if(!isSetting){//����ʱ��������back��
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onPause() {//����������ķ���
	    super.onPause();
	    ActivityManager activityManager = (ActivityManager) getApplicationContext()
	                .getSystemService(Context.ACTIVITY_SERVICE);
	   // ����û�������������ѵ�ǰ����activity�ᵽ��ǰ����������������
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
