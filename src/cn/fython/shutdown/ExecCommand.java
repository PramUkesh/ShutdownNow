package cn.fython.shutdown;

import java.io.DataOutputStream;

import android.util.Log;

public final class ExecCommand {
	
	private final static String TAG = "ExecCommand";
	
	public static int execRooted(String command) {
		return exec(command, true);
	}

	public static int exec(String command) {
		return exec(command, false);
	}

	protected static int exec(String command, boolean isNeedRoot) {
		try {
			Process androidCommand = null;
			if (isNeedRoot) {
				androidCommand = Runtime.getRuntime().exec("su");
			} else {
				androidCommand = Runtime.getRuntime().exec("echo AutoCommand");
			}
			DataOutputStream output = new DataOutputStream(androidCommand.getOutputStream());
			output.writeBytes(command + "\n");
			output.flush();
			output.writeBytes("exit\n");
			output.flush();
			androidCommand.waitFor();
			Log.i(TAG, "Command Succeed : back " + androidCommand.exitValue());
			return androidCommand.exitValue();
		} catch (Throwable e) {
			Log.e(TAG, "Command failed : The original command is " + command, e);
			return -1;
		}
	}

	protected static boolean isRooted() {
		int i = exec("cd /system\nls", true);
		if (i != -1 & i != 1) {
			return true;
		} else {
			return false;
		}
	}
	
}
