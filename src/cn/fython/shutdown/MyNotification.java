package cn.fython.shutdown;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

@SuppressLint("NewApi")
public class MyNotification {

	private static boolean useNewApi;
	private static NotificationManager nm;
	private static Notification n;
	
	private static Intent intent, intent0;
	private static PendingIntent pi0, pi1;
	
	@SuppressWarnings("deprecation")
	public static void create(Context arg0, long time){
		useNewApi = Build.VERSION.SDK_INT >= 11;
		
		nm = (NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
		
		intent = new Intent(arg0, ShutdownReceiver.class);
		intent.setAction("cancel");
		pi0 = PendingIntent.getBroadcast(arg0, 0, intent, 0);
		
		intent0 = new Intent(arg0, ShutdownReceiver.class);
		intent0.setAction("now");
		pi1 = PendingIntent.getBroadcast(arg0, 0, intent0, 0);
		
		if (useNewApi){
			if (Build.VERSION.SDK_INT >= 16){
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(time);
				n = new Notification.Builder(arg0).setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle(arg0.getString(R.string.tips_readytoshutdown))
						.setContentText(c.getTime().toString())
						.setDefaults(Notification.DEFAULT_ALL)
						.setTicker(arg0.getString(R.string.tips_readytoshutdown_short))
						.setLights(Color.RED, 1000, 500)
						.addAction(android.R.drawable.ic_menu_delete, arg0.getString(R.string.item_deletetime), pi0)
						.addAction(android.R.drawable.ic_menu_send, arg0.getString(R.string.item_launchnow), pi1)
						.setAutoCancel(true)
						.setOngoing(true)
						.build();
			} else {
				n = new Notification.Builder(arg0).setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle(arg0.getString(R.string.tips_readytoshutdown))
						.setContentText(arg0.getString(R.string.tips_clicktocancel))
						.setContentIntent(pi0)
						.setDefaults(Notification.DEFAULT_ALL)
						.setTicker(arg0.getString(R.string.tips_readytoshutdown_short))
						.setLights(Color.RED, 1000, 500)
						.setAutoCancel(true)
						.setOngoing(true)
						.build();
			}
		} else {
			n = new Notification();
			n.icon = R.drawable.ic_launcher;
			n.setLatestEventInfo(arg0,
					arg0.getString(R.string.tips_readytoshutdown),
					arg0.getString(R.string.tips_clicktocancel),
					pi0);
		}
		
		nm.notify(0, n);
	}
	
	public static void cancel(Context arg0){
		nm.cancelAll();
	}
	
}
