package com.kanbaru.digitalunlock;

import com.kanbaru.tools.CloseActivityClass;
import com.kanbaru.tools.MyReceiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	Button btn_cancel,btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_cancel=(Button)findViewById(R.id.button_cancel);
        btn_next=(Button)findViewById(R.id.button_nextstep);
        CloseActivityClass.activityList.add(this);
        loadListener();
    }
    
    private void loadListener(){
    	if(btn_next!=null)
    		btn_next.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					Intent i=new Intent(MainActivity.this,ImgChooseActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					startActivity(i);
				}
			});
    	if(btn_cancel!=null)
    		btn_cancel.setOnClickListener(new OnClickListener(){			
				@Override
				public void onClick(View v) {
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			});
    }
}
