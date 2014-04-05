package cn.fython.shutdown;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
	
	Button btn_shutdown, btn_reboot;
	AlertDialog dialogExit;
	
	private final static String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (!ExecCommand.isRooted()){
			Log.v(TAG, "No rooted!");
			showUnrootedDialog();
		}
		
		btn_shutdown = (Button) findViewById(R.id.btn_shutdown);
		btn_reboot = (Button) findViewById(R.id.btn_reboot);
		
		btn_shutdown.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				int i = ExecCommand.execRooted("reboot -p");
				if (i == -1 | i == 1){
					Log.v(TAG, "No rooted!");
					showUnrootedDialog();
				}
			}
			
		});
		
		btn_reboot.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				int i = ExecCommand.execRooted("reboot");
				if (i == -1 | i == 1){
					Log.v(TAG, "No rooted!");
					showUnrootedDialog();
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItemCompat.setShowAsAction(
				menu.add(R.string.item_settime)
				.setIcon(android.R.drawable.ic_menu_recent_history),
				MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menu){
		switch (menu.getItemId()){
		case 0:
			Intent intent = new Intent(getApplicationContext(), SetTimeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(menu);
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
	
}
