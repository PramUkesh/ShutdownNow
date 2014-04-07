package cn.fython.shutdown;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

public class SetTimeActivity extends ActionBarActivity {

	private final static String TAG = "SetTimeActivity";
	private ActionBar ab;
	private TimePicker mTimePicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ab = getSupportActionBar();
		
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(R.string.item_settime);
		
		String getData = getIntent().getStringExtra("targetTime");
		
		setContentView(R.layout.activity_settime);
		
		mTimePicker = (TimePicker) findViewById(R.id.timePicker);
		if (getData != null){
			mTimePicker.setCurrentHour(Integer.parseInt(getData.substring(0, 2)));
			mTimePicker.setCurrentMinute(Integer.parseInt(getData.substring(3, 5)));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItemCompat.setShowAsAction(
				menu.add(android.R.string.ok)
				.setIcon(android.R.drawable.ic_menu_send),
				MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menu){
		switch (menu.getItemId()){
		case 0:
			Log.i(TAG, "Set time!");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY,
					mTimePicker.getCurrentHour());
			calendar.set(Calendar.MINUTE,
					mTimePicker.getCurrentMinute());
			calendar.set(Calendar.SECOND, 0);
			
			Intent intent = new Intent();
			intent.putExtra("time", calendar.getTimeInMillis());
			intent.putExtra("targetTime",
					(mTimePicker.getCurrentHour().toString().length() == 1
					? "0" + mTimePicker.getCurrentHour() : mTimePicker.getCurrentHour())
					+ ":"
					+ (mTimePicker.getCurrentMinute().toString().length() == 1
					? "0" + mTimePicker.getCurrentMinute() : mTimePicker.getCurrentMinute()));
			setResult(1, intent);
			
			finish();
			break;
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(menu);
	}
}
