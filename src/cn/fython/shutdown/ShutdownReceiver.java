package cn.fython.shutdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShutdownReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		ShutdownManager.readConfig(arg0);
		int mode = ShutdownManager.getMode();
		
		ShutdownManager.setTime(0);
		ShutdownManager.saveConfig(arg0);
		
		ShutdownManager.shutdown(mode);
	}

}
