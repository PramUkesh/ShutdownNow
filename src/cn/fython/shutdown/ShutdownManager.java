package cn.fython.shutdown;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ShutdownManager {
	
	private static int time = 0, mode = 0;
	private static long clockmiles = 0;
	public static boolean isRunning = false;
	
	private static AlarmManager am;
	private static PendingIntent pendingIntent;

	private final static String TAG = "ShutdownManager";
	
	public final static int TIME_NONE = 0, TIME_ONTIME = 1, TIME_WHENMUSICSTOP = 2,
			SHUTDOWN = 0, REBOOT = 1, RECOVERY = 2;
	
	public static int getTime(){
		return time;
	}
	
	public static int getMode(){
		return mode;
	}
	
	public static void setTime(int when){
		time = when;
	}
	
	public static void setClock(long l){
		clockmiles = l;
	}
	
	public static void setMode(int target){
		mode = target;
	}
	
	public static void start(Context context){
		Log.i(TAG, "œÏ”¶ShutdownManager…Ë÷√");
		Log.i(TAG, "time=" + time + ", mode=" + mode + ", clockmiles=" + clockmiles);
		switch (time){
		case TIME_NONE:
			shutdown();
			break;
		case TIME_ONTIME:
			Intent intent = new Intent(context, ShutdownReceiver.class);
			
			if (pendingIntent == null){
				pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
			}
			if (am == null){
				am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			}

			isRunning = true;
			am.set(AlarmManager.RTC_WAKEUP, clockmiles, pendingIntent);
			
			break;
		}
	}
	
	public static void stop(Context context){
		if (!isRunning){
			Log.e(TAG, "Alarm hasn't been running.");
			return;
		}
		if (am == null){
			am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		}
		try {
			am.cancel(pendingIntent);
			isRunning = false;
		} catch (NullPointerException e){
			Log.e(TAG, "pendingIntent is null.");
		}
	}
	
	public static boolean shutdown(){
		int i;
		switch (mode){
		case SHUTDOWN:
			i = ExecCommand.execRooted("reboot -p");
			if (i == -1 | i == 1){
				Log.v(TAG, "No rooted!");
				return false;
			}
			return true;
		case REBOOT:
			i = ExecCommand.execRooted("reboot");
			if (i == -1 | i == 1){
				Log.v(TAG, "No rooted!");
				return false;
			}
			return true;
		case RECOVERY:
			i = ExecCommand.execRooted("reboot recovery");
			if (i == -1 | i == 1){
				Log.v(TAG, "No rooted!");
				return false;
			}
			return true;
		}
		return false;
	}
	
	public static boolean shutdown(int target){
		setMode(target);
		return shutdown();
	}
	
	public static void readConfig(Context context){
		SharedPreferences pref = context.getSharedPreferences("ShutdownManager", 0);
		time = pref.getInt("time", 0);
		mode = pref.getInt("mode", 0);
		isRunning = pref.getBoolean("isRunning", false);
	}
	
	public static void saveConfig(Context context){
		SharedPreferences pref = context.getSharedPreferences("ShutdownManager", 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("time", time);
		editor.putInt("mode", mode);
		editor.putBoolean("isRunning", isRunning);
		editor.commit();
	}
}
