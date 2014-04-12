package cn.fython.shutdown;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

@SuppressLint("NewApi")
public class MyNotification {

	private static boolean useNewApi;
	private static NotificationManager nm;
	private static Notification n;
	
	@SuppressWarnings("deprecation")
	public static void create(Context arg0){
		useNewApi = Build.VERSION.SDK_INT >= 11;
		
		nm = (NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
		
		if (useNewApi){
			n = new Notification.Builder(arg0).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(arg0.getString(R.string.tips_readytoshutdown))
					.setDefaults(Notification.DEFAULT_ALL)
					.setTicker(arg0.getString(R.string.tips_readytoshutdown_short))
					.setLights(Color.RED, 1000, 500)
					.setAutoCancel(true)
					.build();
			
		} else {
			n = new Notification(R.drawable.ic_launcher, arg0.getString(R.string.tips_readytoshutdown), System.currentTimeMillis());
		}
		
		nm.notify(0, n);
	}
	
}
