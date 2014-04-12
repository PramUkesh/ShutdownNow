package cn.fython.shutdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CountdownReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		ShutdownManager.shutdown(arg0, arg1.getIntExtra("mode", 0));
	}

}
