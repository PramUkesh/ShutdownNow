package cn.fython.shutdown;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShortcutActivity extends ActionBarActivity{

	private Intent shortcut;
	private ListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		shortcut = new Intent(getApplicationContext(), MainActivity.class);
		shortcut.setAction("launch");
		
		setContentView(R.layout.activity_shortcut);
		
		ArrayList<String> title = new ArrayList<String>();
		title.add(getString(R.string.btn_shutdown));
		title.add(getString(R.string.btn_reboot));
		title.add(getString(R.string.btn_recovery));
		
		mListView = (ListView) findViewById(R.id.lv_shortcut);
		mListView.setAdapter(new MyAdapter(getApplicationContext(), title));
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String name = null;
				
				switch (arg2){
				case 0:
					shortcut.putExtra("mode", ShutdownManager.SHUTDOWN);
					name = getString(R.string.btn_shutdown);
					break;
				case 1:
					shortcut.putExtra("mode", ShutdownManager.REBOOT);
					name = getString(R.string.btn_reboot);
					break;
				case 2:
					shortcut.putExtra("mode", ShutdownManager.RECOVERY);
					name = getString(R.string.btn_recovery);
					break;
				}
				
				Intent i = new Intent();
				Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher);
				i.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcut);
				i.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
				
				shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
				shortcut.putExtra("duplicate", false);
				
				setResult(RESULT_OK, i);
				finish();
			}
			
		});
	}
	
	class MyAdapter extends BaseAdapter{

		private Context mContext;
		private ArrayList<String> title;
		
		public MyAdapter(Context context, ArrayList<String> title){
			super();
			this.mContext = context;
			this.title = title;
		}
		
		@Override
		public int getCount() {
			return title.size();
		}

		@Override
		public String getItem(int arg0) {
			return title.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			TextView tv = new TextView(mContext);
			tv.setTextAppearance(mContext, android.R.attr.textAppearanceLarge);
			tv.setTextColor(Color.BLACK);
			tv.setPadding(20, 24, 0, 24);
			tv.setText(title.get(arg0));
			return tv;
		}
		
	}
}
