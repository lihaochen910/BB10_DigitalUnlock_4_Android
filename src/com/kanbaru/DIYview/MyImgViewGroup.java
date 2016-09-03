package com.kanbaru.DIYview;


import java.util.ArrayList;
import java.util.List;

import com.kanbaru.digitalunlock.CheckPointActivity;
import com.kanbaru.digitalunlock.R;
import com.kanbaru.tools.CloseActivityClass;

import android.R.drawable;
import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MyImgViewGroup extends AbsoluteLayout{
	Activity CheckPoint_Activity=null;
	public SharedPreferences SP;
	private View rootView;
	public int lastX;
	public int lastY;
	private int random;//密码随机数
	private int password_Num;
	private int PW_x,PW_y;
	private Activity act;
	private Context mContext;
	public ViewGroup mViewGroup;
	private MyImgView tmpImgView;
	private List<MyImgView> mImgViewList;
	boolean have_a_PW_Num=false;
	
	public static boolean PW_found_flag=false;
	int[] leftTop;
	int[] leftBottom;
	int[] rightTop;
	int[] rightBottom;
	int[] location=new int[2];
	public MyImgViewGroup(Context context) {
		super(context);
		mViewGroup=(ViewGroup)((Activity) context).findViewById(android.R.id.content);
	}
	public MyImgViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		mViewGroup=(ViewGroup)((Activity) context).findViewById(android.R.id.content);
	}
	public MyImgViewGroup(Context context,int password_Num,int PW_x,int PW_y) {
		super(context);
		mContext=context;
		SP=mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
		this.password_Num=password_Num;
		this.PW_x=PW_x;
		this.PW_y=PW_y;
		
		mViewGroup=(ViewGroup)((Activity)mContext).findViewById(android.R.id.content);
		this.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
		mImgViewList=new ArrayList<MyImgView>();
		/*---------------------------------------.
		  | ::       在绘制时可以超出父容器的边界	  :: | 
		  '---------------------------------------*/
		mViewGroup.setClipChildren(false);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		/*---------------------------------------.
		  | ::     	     触摸点坐标	     	:: | 
		  '---------------------------------------*/
        int touch_x = (int)event.getX();
        int touch_y = (int)event.getY();
        int touch_raw_x=(int)event.getRawX();
        int touch_raw_y=(int)event.getRawY();
        //getPoint();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
        	lastX=touch_x;
        	lastY=touch_y;
                break;
        case MotionEvent.ACTION_MOVE:
        	
        			int offX = touch_x - lastX;
                    int offY = touch_y - lastY;
                    Log.i("触摸点坐标", "("+touch_raw_x+","+touch_raw_y+")");
                    
                    layout(getLeft()+offX, getTop()+offY,
                    		getRight()+offX , getBottom()+offY);
                    
                   	//this.scrollTo(-(getScrollX()+offX), -(getScrollY()+offY));
                    //scrollBy(-offX/50, -offY/50);
                   	
        	//invalidate();
                break;
        case MotionEvent.ACTION_UP://手指离开屏幕时会检查密码数字的位置
        	Log.i("MotionEvent.ACTION_UP", "!");
        	if(check()){
        		Editor editor=SP.edit();
        		editor.putBoolean("islock", false);
        		editor.commit();
        		CloseActivityClass.exitClient(mContext);
        		System.exit(0);
        	}else {
        		Toast.makeText(mContext,"密码错误",Toast.LENGTH_SHORT).show();
        		/*
        		mViewGroup.removeAllViews();
        		mImgViewList=new ArrayList<MyImgView>();
        		FrameLayout f=new FrameLayout(mContext);
        		CheckPointActivity.loadBG(f);
        		mViewGroup.addView(f);
        		*/	
        		act=(Activity)mContext;
        		act.recreate();
        		
        	}
                break;
        case MotionEvent.ACTION_CANCEL:
        	Log.i("MotionEvent.ACTION_CANCEL", "!");
            break;
        }
     	return true;
	}
	public void creat(Context context,int x,int y){
		if(mViewGroup!=null){
			initMyImgView(context,x,y);
			mViewGroup.addView(this, new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT, x, y));
			}
		}
	
	public void initMyImgView(Context context,int x,int y){//生成子控件 7x12
		/*---------------------------------------.
		  | ::    缺陷:有很小概率不会生成密码数字   :: | 
		  '---------------------------------------*/
		final int old_y=y;
		int line_Num=1;//行号
		int column_Num=1;//列号
		while(x<=400){
			while (y<=650) {
				tmpImgView=new MyImgView(context,line_Num, column_Num);
				tmpImgView.setImageDrawable(initRandomImg());
				mImgViewList.add(tmpImgView);//把数字对象装入数组
							
				this.addView(tmpImgView, new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, dip2px(context, x),  dip2px(context, y)));
				//Log.w("生成控件test坐标", "("+x+","+y+")");
				y+=50;
			}
			line_Num++;
			column_Num=1;
			y=old_y;
			x+=50;
		}	
		
		
	}
	Drawable initRandomImg(){//生成随机密码图片
		int id0=R.drawable.num_0;int id1=R.drawable.num_1;
		int id2=R.drawable.num_2;int id3=R.drawable.num_3;
		int id4=R.drawable.num_4;int id5=R.drawable.num_5;
		int id6=R.drawable.num_6;int id7=R.drawable.num_7;
		int id8=R.drawable.num_8;int id9=R.drawable.num_9;
		int tmp = id0;
		int random=(int)(Math.random()*10);
		if(random==password_Num){
			have_a_PW_Num=true;
			tmpImgView.isPW_Num=true;
		}
		switch(random) {
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
		return getResources().getDrawable(tmp);
	}
	public static int dip2px(Context context, float dpValue) {//dip单位转px单位
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	public boolean check(){//检查密码数字的位置
		for(int i=0;i<mImgViewList.size();i++){
			MyImgView tmpImgView=mImgViewList.get(i);
			if(tmpImgView.isPW_Num){
				int tmp_x=tmpImgView.getWidget_loc()[0]-20,tmp_y=tmpImgView.getWidget_loc()[1]-20;
				Log.w("控件坐标", tmp_x+","+tmp_y);
				Log.i("密码坐标", PW_x+","+PW_y);
				/*---------------------------------------.
				  | ::     	     判定范围     	:: | 
				  '---------------------------------------*/
				
				int D_value=SP.getInt("D_value", 5);//误差值
				
				if((PW_x-D_value)<=tmp_x&&tmp_x<=(PW_x+D_value)){
					if((PW_y-D_value)<=tmp_y&&tmp_y<=(PW_y+D_value)){
						return true;
					}
				}
			}
		}
		return false;
	}
	public void getPoint(){
		/*---------------------------------------.
		  | ::     	     控件坐标	     	:: | 
		  '---------------------------------------*/
		//this.getLocationInWindow(location);
		this.getLocationOnScreen(location);
		int y=location[1];
		 	int[] leftTop={getLeft(),y};
	        int[] leftBottom={getLeft(),y+getHeight()};
	        int[] rightTop={getLeft()+getWidth(),y};
	        int[] rightBottom={getLeft()+getWidth(),y+getHeight()};
	        Log.w("左上角坐标",location[0]+","+location[1]);
	        Log.i("左下角坐标",leftBottom[0]+","+leftBottom[1]);
	        Log.i("右上角坐标",rightTop[0]+","+rightTop[1]);
	        Log.i("右下角坐标",rightBottom[0]+","+rightBottom[1]);
	}

}
