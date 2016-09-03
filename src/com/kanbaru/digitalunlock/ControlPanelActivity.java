package com.kanbaru.digitalunlock;

import com.kanbaru.tools.CloseActivityClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Program;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ControlPanelActivity extends Activity {
	private SharedPreferences SP;
	private Editor SPEditor;
	private static int x;
	private static int y;
	private static int password_Num;
	private static int D_value;
	private static boolean Main_switch;
	TextView textView_status,sBar_TextView,textView_cp_title,textView_Debug;
	CheckBox Main_switch_box;
	Button button;
	SeekBar sBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_control_panel);
		CloseActivityClass.activityList.add(this);
		
		SP=this.getSharedPreferences("data", Context.MODE_PRIVATE);
		SPEditor=SP.edit();
		
		textView_cp_title=(TextView) findViewById(R.id.textView_cp_title);
		textView_status=(TextView) findViewById(R.id.textView_status);
		Main_switch_box=(CheckBox) findViewById(R.id.checkBox1);
		button=(Button) findViewById(R.id.button1);
		sBar = (SeekBar)findViewById(R.id.seekBar1);
		D_value=SP.getInt("D_value", -1);
		if(D_value>0)
			sBar.setProgress(D_value);
		
		sBar_TextView = (TextView)findViewById(R.id.textView_sBar);
		sBar_TextView.setText("检测灵敏度:"+sBar.getProgress());
		textView_Debug=(TextView) findViewById(R.id.textView_Debug);
		textView_Debug.setVisibility(View.INVISIBLE);//默认不可见
		
		Main_switch=SP.getBoolean("Master_switch", false);
		if(Main_switch)
			Main_switch_box.setTextColor(Color.parseColor("#00FF00"));
		else Main_switch_box.setTextColor(Color.parseColor("#FF0000"));
		Main_switch_box.setChecked(Main_switch);
		Main_switch_box.setText("主开关："+Main_switch);
		
		password_Num=SP.getInt("PW_Num", -1);
		x=SP.getInt("widget_center_x", 0);
		y=SP.getInt("widget_center_y", 0);
		refreshDebugInfo();
		if(password_Num==-1){
			//textView_status.setText("\n未找到");
			Main_switch_box.setEnabled(false);
			sBar.setEnabled(false);
		}else textView_status.setText("\n已找到");
		loadListener();
		
	}
	void refreshDebugInfo(){
		String Detail="全局开关="+SP.getBoolean("Master_switch", false)+"\n"
				+"密码数字="+password_Num+"\n"
				+"密码数字坐标=("+x+","+y+")\n"
				+"seekBar_Value="+sBar.getProgress()+"\n"
				+"灵敏度="+(sBar.getProgress()+3);
		textView_Debug.setText(Detail);
	}
	void loadListener(){
		textView_cp_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(textView_Debug.getVisibility()==View.INVISIBLE)
					textView_Debug.setVisibility(View.VISIBLE);
				else textView_Debug.setVisibility(View.INVISIBLE);	
			}
		});
		button.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i=new Intent(ControlPanelActivity.this,MainActivity.class);
       		 	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       		 	startActivity(i);
       		 	
			}
		});
		Main_switch_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Main_switch_box.setText("主开关："+isChecked);
				if(isChecked)
					Main_switch_box.setTextColor(Color.parseColor("#00FF00"));
				else Main_switch_box.setTextColor(Color.parseColor("#FF0000"));
				
				SPEditor.putBoolean("Master_switch", isChecked);
				SPEditor.commit();
				refreshDebugInfo();
			}
		});
		sBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				progress+=3;
				String tString="检测灵敏度:"+progress+"  (changed)";
				sBar_TextView.setText(tString);
				SPEditor.putInt("D_value", progress);
				SPEditor.commit();
				refreshDebugInfo();
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
	}
	
}
