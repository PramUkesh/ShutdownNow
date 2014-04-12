package cn.fython.shutdown;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity {
	
	Button btn_shutdown, btn_reboot, btn_recovery;
	AlertDialog dialogExit, dialogHelp, dialogShutdown;
	
	private static String targetTime;
	
	private final static String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ShutdownManager.readConfig(getApplicationContext());
		
		if (!ExecCommand.isRooted()){
			Log.v(TAG, "No rooted!");
			showUnrootedDialog();
		}
		
		btn_shutdown = (Button) findViewById(R.id.btn_shutdown);
		btn_reboot = (Button) findViewById(R.id.btn_reboot);
		btn_recovery = (Button) findViewById(R.id.btn_recovery);
		
		btn_shutdown.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				if (ShutdownManager.getTime() != ShutdownManager.TIME_NONE){
					ShutdownManager.stop(getApplicationContext());
					ShutdownManager.setMode(ShutdownManager.SHUTDOWN);
					ShutdownManager.start(getApplicationContext());
					if (Build.VERSION.SDK_INT >= 11){
						MainActivity.this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
					}
					Toast.makeText(getApplicationContext(), getString(R.string.tips_finishsetting), Toast.LENGTH_SHORT).show();
					return;
				}
				showShutdownDialog(ShutdownManager.SHUTDOWN);
			}
			
		});
		
		btn_reboot.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				if (ShutdownManager.getTime() != ShutdownManager.TIME_NONE){
					ShutdownManager.stop(getApplicationContext());
					ShutdownManager.setMode(ShutdownManager.REBOOT);
					ShutdownManager.start(getApplicationContext());
					if (Build.VERSION.SDK_INT >= 11){
						MainActivity.this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
					}
					Toast.makeText(getApplicationContext(), getString(R.string.tips_finishsetting), Toast.LENGTH_SHORT).show();
					return;
				}
				showShutdownDialog(ShutdownManager.REBOOT);
			}
			
		});
		
		btn_recovery.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				if (ShutdownManager.getTime() != ShutdownManager.TIME_NONE){
					ShutdownManager.stop(getApplicationContext());
					ShutdownManager.setMode(ShutdownManager.RECOVERY);
					ShutdownManager.start(getApplicationContext());
					if (Build.VERSION.SDK_INT >= 11){
						MainActivity.this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
					}
					Toast.makeText(getApplicationContext(), getString(R.string.tips_finishsetting), Toast.LENGTH_SHORT).show();
					return;
				}
				showShutdownDialog(ShutdownManager.RECOVERY);
			}
			
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();

		if (Build.VERSION.SDK_INT >= 11){
			if (ShutdownManager.getTime() == ShutdownManager.TIME_NONE | ShutdownManager.getClock() == 0){
				MenuItemCompat.setShowAsAction(
						menu.add(R.string.item_settime)
						.setIcon(R.drawable.ic_menu_recent_history),
						MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			} else {
				MenuItemCompat.setShowAsAction(menu.add(targetTime), MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
				MenuItemCompat.setShowAsAction(
						menu.add(R.string.item_deletetime)
						.setIcon(R.drawable.ic_menu_delete),
						MenuItemCompat.SHOW_AS_ACTION_IF_ROOM|MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
			}
		} else {
			MenuItemCompat.setShowAsAction(
					menu.add(R.string.item_settime)
					.setIcon(R.drawable.ic_menu_recent_history),
					MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			MenuItemCompat.setShowAsAction(menu.add(targetTime), MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
			MenuItemCompat.setShowAsAction(
					menu.add(R.string.item_deletetime)
					.setIcon(R.drawable.ic_menu_delete),
					MenuItemCompat.SHOW_AS_ACTION_IF_ROOM|MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		}
		
		MenuItemCompat.setShowAsAction(
				menu.add(R.string.item_help)
				.setIcon(R.drawable.ic_menu_help), 
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menu){
		if (menu.getTitle() == getString(R.string.item_settime)){
			Intent intent = new Intent(MainActivity.this, SetTimeActivity.class);
			startActivityForResult(intent, 0);
		}
		
		if (menu.getTitle() == getString(R.string.item_deletetime)){
			ShutdownManager.setTime(ShutdownManager.TIME_NONE);
			ShutdownManager.stop(getApplicationContext());
			if (Build.VERSION.SDK_INT >= 11){
				MainActivity.this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
			}
			Toast.makeText(getApplicationContext(), getString(R.string.tips_cancelsetting), Toast.LENGTH_SHORT).show();
		}
		
		if (menu.getTitle() == getString(R.string.item_help)){
			showHelpDialog();
		}
		
		if (menu.getTitle() == targetTime){
			Intent intent = new Intent(MainActivity.this, SetTimeActivity.class);
			intent.putExtra("targetTime", targetTime);
			startActivityForResult(intent, 0);
		}
		
		return super.onOptionsItemSelected(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		Log.i(TAG, "onActivityResult requestCode=" + requestCode + ", resultCode=" + resultCode);
		switch (resultCode){
		case 1:
			ShutdownManager.setTime(ShutdownManager.TIME_ONTIME);
			ShutdownManager.setClock(data.getLongExtra("time", 0));
			if (ShutdownManager.isRunning){
				ShutdownManager.start(getApplicationContext());
			}
			targetTime = data.getStringExtra("targetTime");
			if (Build.VERSION.SDK_INT >= 11){
				MainActivity.this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
			}
			Toast.makeText(getApplicationContext(), getString(R.string.tips_selectmode), Toast.LENGTH_SHORT).show();
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void showUnrootedDialog(){
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
				System.exit(0);
			}
		};
		
		dialogExit = new AlertDialog.Builder(this)
		.setTitle(getString(R.string.title_sorry))
		.setMessage(getString(R.string.context_norooted))
		.setPositiveButton(android.R.string.cancel, listener).show();
		
		dialogExit.setOnCancelListener(new OnCancelListener(){

			@Override
			public void onCancel(DialogInterface arg0) {
				finish();
				System.exit(0);
			}
			
		});
		
		dialogExit.show();
	}
	
	private void showHelpDialog(){
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				dialogHelp.dismiss();
			}
		};
		
		dialogHelp = new AlertDialog.Builder(this)
		.setTitle(getString(R.string.title_help))
		.setMessage(getString(R.string.context_help))
		.setPositiveButton(android.R.string.ok, listener)
		.show();
	}
	
	private void showShutdownDialog(final int what){
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				switch (whichButton){
				case DialogInterface.BUTTON_POSITIVE:
					if (!ShutdownManager.shutdown(getApplicationContext(), what)){
						showUnrootedDialog();
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					dialogHelp.dismiss();
					break;
				}
			}
		};
		
		dialogHelp = new AlertDialog.Builder(this)
		.setTitle(getString(R.string.title_shutdown))
		.setMessage(getString(R.string.context_shutdownnow))
		.setPositiveButton(android.R.string.ok, listener)
		.setNegativeButton(android.R.string.no, listener)
		.show();
	}
	
}
