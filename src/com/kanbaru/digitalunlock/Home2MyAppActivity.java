package com.kanbaru.digitalunlock;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Home2MyAppActivity extends Activity {
	private static SharedPreferences SP;
	List<String> pkgNamesT= new ArrayList<String>();
	List<String> actNamesT= new ArrayList<String>();
	List<String> actLabel= new ArrayList<String>();
	String pkgName,actName;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home2_my_app);
		SP=getSharedPreferences("data", Context.MODE_PRIVATE);
		pkgName = SP.getString("packageName", null);
		actName = SP.getString("activityName", null);
		intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		
		if(pkgName==null|actName==null)
			getHomeScreenApp();
		else{ 
			if(SP.getBoolean("islock", false)){
				Log.w("处于锁定状态","lockFlag=true");
				Intent i=new Intent(this,CheckPointActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
			}else {
				Log.w("未锁定状态","lockFlag=false");
				ComponentName componentName = new ComponentName(pkgName, actName);
				intent.setComponent(componentName);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		}
		
	}
	private void getHomeScreenApp(){
		
		
		List<ResolveInfo> resolveInfos = this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			for (int i = 0; i < resolveInfos.size(); i++) {
				String string = resolveInfos.get(i).activityInfo.packageName;
				Log.w("string",string);
				if (!string.equals(this.getPackageName())) {//排除自己的包名
					pkgNamesT.add(string);
					actLabel.add(getAppLabel(string));
					actNamesT.add(resolveInfos.get(i).activityInfo.name);
					
				}
			}
			CharSequence[] cs = actLabel.toArray(new CharSequence[actLabel.size()]);	
			new AlertDialog.Builder(this).setTitle("请选择解锁后的屏幕").setCancelable(false).setSingleChoiceItems(cs, 0, new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	Editor editor=SP.edit();
			        editor.putString("packageName", pkgNamesT.get(which));
			        editor.putString("activityName", actNamesT.get(which));
			        editor.commit();
			        //originalHome();
			        dialog.dismiss();
			        finish();
			        /*
			        Intent i=new Intent(Home2MyAppActivity.this,CheckPointActivity.class);
					startActivity(i);
					*/
			    }
			}).show();
			
		
	}
	String getAppLabel(String packageName){
		PackageManager packageManager = getApplicationContext().getPackageManager(); 
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = packageManager.getApplicationInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (String)packageManager.getApplicationLabel(applicationInfo);
	}
}
