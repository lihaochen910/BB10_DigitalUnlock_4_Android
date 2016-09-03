package com.kanbaru.digitalunlock;

import java.io.File;

import com.kanbaru.tools.CloseActivityClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class DigiChooseActivity extends Activity implements OnClickListener{
	Button btn_back;
	Button b0,b1,b2,b3,b4,b5,b6,b7,b8,b9;
	FrameLayout FL_background_2;
	Drawable d=null;
	private Uri SavedUri=null;
	private SharedPreferences SP;
	private Editor SPEditor;
	boolean IsDefault;
	private SeekBar sBar;
	private TextView sBar_TextView;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_digi_choose);
		CloseActivityClass.activityList.add(this);
		FL_background_2=(FrameLayout)findViewById(R.id.FrameLayout4);
		sBar=(SeekBar)findViewById(R.id.seekBar1);
		sBar_TextView=(TextView)findViewById(R.id.textView_cp_title);
		sBar_TextView.setText("检测灵敏度:"+sBar.getProgress());
		loadListener();
		SP=this.getSharedPreferences("data", Context.MODE_APPEND);
		SPEditor=SP.edit();
		IsDefault=SP.getBoolean("default", true);
		if(!IsDefault){
			SavedUri=Uri.fromFile(new File(SP.getString("ImgPath","")));
			Log.i("SavedUri=",SavedUri.toString());
			d=Drawable.createFromPath(SP.getString("ImgPath",""));
			FL_background_2.setBackground(d);
		}else{
			FL_background_2.setBackgroundResource(R.drawable.default_lockscreen);
		}
	}
	private void loadListener(){
		btn_back=(Button)findViewById(R.id.button_back_4);
		b0=(Button)findViewById(R.id.button_no_0);
		b1=(Button)findViewById(R.id.button_no_1);
		b2=(Button)findViewById(R.id.button_no_2);
		b3=(Button)findViewById(R.id.button_no_3);
		b4=(Button)findViewById(R.id.button_no_4);
		b5=(Button)findViewById(R.id.button_no_5);
		b6=(Button)findViewById(R.id.button_no_6);
		b7=(Button)findViewById(R.id.button_no_7);
		b8=(Button)findViewById(R.id.button_no_8);
		b9=(Button)findViewById(R.id.button_no_9);
		b0.setOnClickListener(this);b1.setOnClickListener(this);b2.setOnClickListener(this);
		b3.setOnClickListener(this);b4.setOnClickListener(this);b5.setOnClickListener(this);
		b6.setOnClickListener(this);b7.setOnClickListener(this);b8.setOnClickListener(this);
		b9.setOnClickListener(this);
		sBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				progress+=3;
				String tString="检测灵敏度:"+progress;
				sBar_TextView.setText(tString);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {			
				Intent i=new Intent(DigiChooseActivity.this,ImgChooseActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				startActivity(i);
			}
		});
	}
	@Override
	public void onClick(View v) {
		Log.i("onClick()", "");
		 switch (v.getId()){
		 	case R.id.button_no_0:
		 		SPEditor.putInt("PW_Num",0);break;
		 	case R.id.button_no_1:
		 		SPEditor.putInt("PW_Num",1);break;
		 	case R.id.button_no_2:
		 		SPEditor.putInt("PW_Num",2);break;
		 	case R.id.button_no_3:
		 		SPEditor.putInt("PW_Num",3);break;
		 	case R.id.button_no_4:
		 		SPEditor.putInt("PW_Num",4);break;
		 	case R.id.button_no_5:
		 		SPEditor.putInt("PW_Num",5);break;
		 	case R.id.button_no_6:
		 		SPEditor.putInt("PW_Num",6);break;
		 	case R.id.button_no_7:
		 		SPEditor.putInt("PW_Num",7);break;
		 	case R.id.button_no_8:
		 		SPEditor.putInt("PW_Num",8);break;
		 	case R.id.button_no_9:
		 		SPEditor.putInt("PW_Num",9);break;
		 	default:break;
		 }
		 SPEditor.putInt("D_value", sBar.getProgress()+3);
		 SPEditor.commit();
		 Toast.makeText(getApplicationContext(),SP.getInt("PW_Num",0)+"将会作为密码数字",Toast.LENGTH_SHORT).show();
		 if(v.getId()!=R.id.button_back_4){
			 Intent i=new Intent(DigiChooseActivity.this,SetLocationActivity.class);
			 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			 startActivity(i);
		 }
			 
	}
	
}
