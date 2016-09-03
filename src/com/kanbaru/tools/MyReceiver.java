package com.kanbaru.tools;

import com.kanbaru.digitalunlock.CheckPointActivity;
import com.kanbaru.digitalunlock.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
	private Intent i=null;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		 String action = intent.getAction();
		 SharedPreferences SP=context.getSharedPreferences("data", Context.MODE_PRIVATE);
		 if (Intent.ACTION_SCREEN_ON.equals(action)) { // ¿ªÆÁ
             
         } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // ËøÆÁ
            Log.i("ÆÁÄ»¹Ø±Õ","!");
             CheckPointActivity.first_Tip=true;  
         } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // ½âËø
        	 if(SP.getBoolean("Master_switch", false)){
        		 Editor editor=SP.edit();
       				editor.putBoolean("islock", true);
       				editor.commit();
       				
        			 i=new Intent(context,CheckPointActivity.class);
            		 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            		 context.startActivity(i); 		 
        	 }
         }
         
	}

}
