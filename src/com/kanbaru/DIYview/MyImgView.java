package com.kanbaru.DIYview;



import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MyImgView extends ImageView {
	private int lastX;
	private int lastY;
	int widget_center_x=0;
	int widget_center_y=0;
	private boolean portability_status=false;//�Ƿ���ƶ�
	public boolean isPW_Num=false;
	private boolean visibility=true;//�ɼ���
	public int[] line_column=new int[2];
	public MyImgView(Context context){
		super(context);
	}
	public MyImgView(Context context,int line,int column) {
	        super(context);
	        line_column[0]=line;
			line_column[1]=column;
	}
	public MyImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	public int[] getWidget_loc() {
		int[] location=new int[2];
		widget_center_x=getLeft()+(getRight()-getLeft())/2;
        widget_center_y=getTop()+(getBottom()-getTop())/2;
        getLocationOnScreen(location);  
		return location;
	}
	public void change_CanBeListened(){
		if(!portability_status){
			portability_status=true;
		}else portability_status=false;
	}
	/*---------------------------------------.
	  | ::     	     �����¼���Ӧ	     	:: | 
	  '---------------------------------------*/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	
	if(portability_status){
		final int action = event.getAction();
		/*---------------------------------------.
		  | ::     	     ����������	     	:: | 
		  '---------------------------------------*/
        int touch_x = (int)event.getX();
        int touch_y = (int)event.getY();
        int touch_raw_x=(int)event.getRawX();
        int touch_raw_y=(int)event.getRawY();
        /*---------------------------------------.
		  | ::     	     �ؼ����ĵ�����	     	:: | 
		  '---------------------------------------*/
        widget_center_x=getLeft()+(getRight()-getLeft())/2;
        widget_center_y=getTop()+(getBottom()-getTop())/2;
        //Log.w("MyImgView�ؼ����ĵ�����", "("+widget_center_x+","+widget_center_y+")");
        
     switch (action) {
        case MotionEvent.ACTION_DOWN:
        	lastX=touch_x;
        	lastY=touch_y;
                break;
        case MotionEvent.ACTION_MOVE:
        	Log.i("MotionEvent.ACTION_MOVE", "");
        	Log.i("����������", "("+touch_raw_x+","+touch_raw_y+")");
        	/*---------------------------------------.
		  		| ::     	     ����720p�ı߽�����	     	:: | 
		  		'---------------------------------------*/
        	if(touch_raw_y>=150&&touch_raw_y<=1280){
        		if(touch_raw_x>=0&&touch_raw_x<=720){
        			/*---------------------------------------.
      		  		| ::     	     �����ֵ	     	:: | 
      		  		'---------------------------------------*/
        			
        		}
        	}
        	int offX = touch_x - lastX;
            int offY = touch_y - lastY;
            layout(getLeft()+offX, getTop()+offY,
            		getRight()+offX , getBottom()+offY);
                break;
        case MotionEvent.ACTION_UP:  
                break;
        case MotionEvent.ACTION_CANCEL:
            	break;
        }
     	return true;
		}
	else return false;
	}
	
}
