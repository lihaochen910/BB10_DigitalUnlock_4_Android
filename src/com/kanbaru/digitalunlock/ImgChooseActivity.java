package com.kanbaru.digitalunlock;

import java.io.File;
import java.io.FileOutputStream;

import com.kanbaru.tools.CloseActivityClass;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class ImgChooseActivity extends Activity {
	Button btn_back,btn_choose_img,btn_default_img;
	FrameLayout FL_background;
	Drawable d=null;
	private Uri tempUri=null;
	private SharedPreferences SP;
	Editor SPEditor;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_img_choose);
		CloseActivityClass.activityList.add(this);
		btn_back=(Button)findViewById(R.id.button_back);
		btn_choose_img=(Button)findViewById(R.id.button_choose_img);
		btn_default_img=(Button)findViewById(R.id.button_img_default);
		FL_background=(FrameLayout)findViewById(R.id.FrameLayout2);
		loadListener();
		
		File f=new File(getSDPath()+"/Android/lockscreen.jpg");
        Log.i("f=new File()",getSDPath()+"/Android/lockscreen.jpg");
        if(f.exists())
            f.delete();
        tempUri=Uri.fromFile(f);
        SP=this.getSharedPreferences("data", Context.MODE_APPEND);
        SPEditor=SP.edit();
        SPEditor.putString("SDPath", getSDPath());
        SPEditor.putString("Uri", tempUri.toString());
        SPEditor.putString("ImgPath",Uri2Path(tempUri));
        SPEditor.commit();
        Log.i("tempUri=",tempUri+"");
        Log.i("Uri2Path(tempUri)=",Uri2Path(tempUri));
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	if(requestCode!=RESULT_CANCELED){
		switch (requestCode) {
	        case PHOTO_REQUEST_TAKEPHOTO:
	        	Log.i("case PHOTO_REQUEST_TAKEPHOTO","requestCode="+requestCode);
	        	Log.i("case PHOTO_REQUEST_TAKEPHOTO","resultCode="+resultCode);
	            startPhotoZoom(Uri.fromFile(new File(Uri2Path(data.getData()))));
	            break;
	        case PHOTO_REQUEST_GALLERY:
	        	Log.i("case PHOTO_REQUEST_GALLERY","requestCode="+requestCode);
	        	Log.i("case PHOTO_REQUEST_GALLERY","resultCode="+resultCode);
	            if (data != null)
	                startPhotoZoom(data.getData());
	            break;
	        case PHOTO_REQUEST_CUT:
	        	Log.i("case PHOTO_REQUEST_CUT","requestCode="+requestCode);
	        	Log.i("case PHOTO_REQUEST_CUT","resultCode="+resultCode);
	        	if (resultCode == RESULT_OK) {
	        		setPicToView();
	 	            Intent i=new Intent(ImgChooseActivity.this,DigiChooseActivity.class);
	 	            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	 				startActivity(i);
	        	}    
	            break;
	        }
				
	}
		
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 9);
        intent.putExtra("aspectY", 16);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 720);
        intent.putExtra("outputY", 1280);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("return-data", false);
        
        Log.i("intent.putExtra();","intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        Log.i("startActivityForResult();","startActivityForResult(intent, PHOTO_REQUEST_CUT);");
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
	private void setPicToView() {
		Log.i("d=d.createFromPath()",Uri2Path(tempUri));
    	d=Drawable.createFromPath(Uri2Path(tempUri));
    	FL_background.setBackground(d);
    	SPEditor.putBoolean("default", false);
		SPEditor.commit();
    	Log.i("FL_background.setBackground()","");
    }
	
	private void loadListener(){
		if(btn_back!=null)
			btn_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i=new Intent(ImgChooseActivity.this,MainActivity.class);
					startActivity(i);
				}
			});
		if(btn_choose_img!=null)
			btn_choose_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
					i.addCategory(Intent.CATEGORY_OPENABLE);
					i.setType("image/*");
					startActivityForResult(Intent.createChooser(i,"选择图片"), 2);				
				}
			});
		if(btn_default_img!=null)
			btn_default_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SPEditor.putBoolean("default", true);
					SPEditor.commit();
					Intent i=new Intent(ImgChooseActivity.this,DigiChooseActivity.class);
					startActivity(i);
					
				}
			});
			
	}
	/*---------------------------------------.
		| ::     	     其他功能	     	:: | 
		'---------------------------------------*/
	private String Uri2Path(Uri ImgUri) {
		    if ( null == ImgUri ) return null;
		    final String scheme = ImgUri.getScheme();
		    String data = null;
		    if ( scheme == null )
		        data = ImgUri.getPath();
		    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
		        data = ImgUri.getPath();
		    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
		        Cursor cursor = this.getContentResolver().query( ImgUri, new String[] { ImageColumns.DATA }, null, null, null );
		        if ( null != cursor ) {
		            if ( cursor.moveToFirst() ) {
		                int index = cursor.getColumnIndex( ImageColumns.DATA );
		                if ( index > -1 ) {
		                    data = cursor.getString( index );
		                }
		            }
		            cursor.close();
		        }
		    }
		    return data;
	}
	private Uri Path2Uri(String path){
		Uri uri=null;
		uri.decode(path);
		Toast.makeText(getApplicationContext(),"Path2Uri return "+uri,Toast.LENGTH_SHORT).show();
		return uri;
	}
	private String FilePath2FileName(String filePath){
		File file=new File(filePath);
		return file.getName();
	}
	private String getSDPath(){ 
	       File sdDir = null; 
	       boolean sdCardExist = Environment.getExternalStorageState()   
	       .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
	       if(sdCardExist)   
	       {                               
	         sdDir = Environment.getExternalStorageDirectory();//获取跟目录
	      }   
	      return sdDir.toString(); 
	}

	public static void copyfile(File fromFile, File toFile,Boolean rewrite){

		if (!fromFile.exists()) {
			return;
		}
		if (!fromFile.isFile()) {
			return ;
		}
		if (!fromFile.canRead()) {
			return ;
		}
		if (!toFile.getParentFile().exists()) {

			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}	
		try{
			java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);
			java.io.FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
		while ((c = fosfrom.read(bt)) > 0) {
			fosto.write(bt, 0, c); //将内容写到新文件当中
		}
			fosfrom.close();
			fosto.close();
		} catch (Exception ex) {
			Log.e("readfile", ex.getMessage());
		}
	}
	
	
}
