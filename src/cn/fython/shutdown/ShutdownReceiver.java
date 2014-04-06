package cn.fython.shutdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ShutdownReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		ShutdownManager.readConfig(arg0);
		int mode = arg1.getIntExtra("mode", 0);
		Log.i("ShutdownReceiver", "mode:" + mode);
		
		ShutdownManager.setTime(0);
		ShutdownManager.saveConfig(arg0);
		
		ShutdownManager.shutdown(arg0, mode);
	}

}
