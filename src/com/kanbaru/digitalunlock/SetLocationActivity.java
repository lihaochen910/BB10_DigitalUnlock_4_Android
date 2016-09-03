package com.kanbaru.digitalunlock;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.kanbaru.DIYview.MyImgView;
import com.kanbaru.tools.CloseActivityClass;

import android.R.drawable;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SetLocationActivity extends Activity{
	private SharedPreferences SP;
	private Editor SPEditor;
	private Button btn_back,btn_next;
	private int pw_Num;
	private FrameLayout FL_background_4;
	private MyImgView img_Num;
	Drawable d=null;
	boolean IsDefault;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		CloseActivityClass.activityList.add(this);
		setContentView(R.layout.activity_set_location);
		FL_background_4=(FrameLayout)findViewById(R.id.FrameLayout4);
		btn_back=(Button)findViewById(R.id.button_back_4);
		btn_next=(Button)findViewById(R.id.button_nextstep_4);
		img_Num=(MyImgView)findViewById(R.id.imageView_demo);
		SP=this.getSharedPreferences("data", Context.MODE_PRIVATE);
		SPEditor=SP.edit();
		IsDefault=SP.getBoolean("default", true);
		if(!IsDefault){
			d=Drawable.createFromPath(SP.getString("ImgPath",""));
			 FL_background_4.setBackground(d);
		}else{
			 FL_background_4.setBackgroundResource(R.drawable.default_lockscreen);
		}
		pw_Num=SP.getInt("PW_Num", 0);
		loadNum();
		loadListener();
		img_Num.change_CanBeListened();
	}
	private void loadNum(){
		int id0=R.drawable.num_0;int id1=R.drawable.num_1;
		int id2=R.drawable.num_2;int id3=R.drawable.num_3;
		int id4=R.drawable.num_4;int id5=R.drawable.num_5;
		int id6=R.drawable.num_6;int id7=R.drawable.num_7;
		int id8=R.drawable.num_8;int id9=R.drawable.num_9;
		int tmp = id0;
		switch (pw_Num) {
			case 0:tmp=id0;break;
			case 1:tmp=id1;break;
			case 2:tmp=id2;break;
			case 3:tmp=id3;break;
			case 4:tmp=id4;break;
			case 5:tmp=id5;break;
			case 6:tmp=id6;break;
			case 7:tmp=id7;break;
			case 8:tmp=id8;break;
			case 9:tmp=id9;break;
			default:break;
		}
		Log.i("int tmp=", tmp+"");
		Drawable d=getResources().getDrawable(tmp);
		img_Num.setImageDrawable(d);
	}
	private int lastX;
	private int lastY;
	int widget_center_x=0;
	int widget_center_y=0;
	private void loadListener(){
		btn_back.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(SetLocationActivity.this,DigiChooseActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SPEditor.putInt("widget_center_x", ((MyImgView) img_Num).getWidget_loc()[0]);
				SPEditor.putInt("widget_center_y", ((MyImgView) img_Num).getWidget_loc()[1]);
				SPEditor.putBoolean("isSetting", true);//设置状态，不会屏蔽back键
				SPEditor.putBoolean("Master_switch", true);//总开关
				SPEditor.commit();
				Log.w("getWidget_loc()", "("+((MyImgView) img_Num).getWidget_loc()[0]+","+((MyImgView) img_Num).getWidget_loc()[1]+")");   
				Toast.makeText(getApplicationContext(),"请记住数字和位置",Toast.LENGTH_SHORT).show();
				Intent i=new Intent(SetLocationActivity.this,CheckPointActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				startActivity(i);
			}
		});
	}
	
}
