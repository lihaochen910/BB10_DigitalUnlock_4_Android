package com.kanbaru.tools;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;

public class MyCrashHandler implements UncaughtExceptionHandler{
	public static final String TAG = "CrashHandler";  
    private static MyCrashHandler INSTANCE = new MyCrashHandler();  
    private Context mContext;  
    private Thread.UncaughtExceptionHandler mDefaultHandler;  
  
    private MyCrashHandler() {  
    }  
  
    public static MyCrashHandler getInstance() {  
        return INSTANCE;  
    }  
  
    public void init(Context ctx) {  
        mContext = ctx;  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        Thread.setDefaultUncaughtExceptionHandler(this);  
    }  
  
    @Override  
    public void uncaughtException(Thread thread, Throwable ex) {  
        // if (!handleException(ex) && mDefaultHandler != null) {  
        // mDefaultHandler.uncaughtException(thread, ex);  
        // } else {  
        // android.os.Process.killProcess(android.os.Process.myPid());  
        // System.exit(10);  
        // }  
        System.out.println("uncaughtException");  
  
        new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();  
                new AlertDialog.Builder(mContext).setTitle("Tip").setCancelable(false)  
                        .setMessage("������ȷ").setNeutralButton("��֪����", new OnClickListener() {  
                            @Override  
                            public void onClick(DialogInterface dialog, int which) {  
                                System.exit(0);  
                            }  
                        })  
                        .create().show();  
                Looper.loop();  
            }  
        }.start();  
    }  
  
    /** 
     * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����. �����߿��Ը����Լ���������Զ����쳣�����߼� 
     * 
     * @param ex 
     * @return true:��������˸��쳣��Ϣ;���򷵻�false 
     */  
    private boolean handleException(Throwable ex) {  
        if (ex == null) {  
            return true;  
        }  
        return true;  
    }  
}
