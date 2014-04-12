package cn.fython.shutdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShutdownReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		ShutdownManager.setTime(0);
		ShutdownManager.isRunning = false;
		ShutdownManager.saveConfig(arg0);
		
		MyNotification.create(arg0);
		ShutdownManager.startCountdown(arg0, arg1.getLongExtra("time", 0), arg1.getIntExtra("mode", 0));
	}

}
