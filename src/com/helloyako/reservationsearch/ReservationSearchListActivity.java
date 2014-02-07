package com.helloyako.reservationsearch;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ReservationSearchListActivity extends Activity {
	
	private ArrayList<AlarmInfo> alarmInfoList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.reservation_search_list);
	    
	    alarmInfoList = new ArrayList<AlarmInfo>();
	    
	    ListView lv = (ListView) findViewById(R.id.listView);
	    lv.setAdapter(new AlarmAdapter(this, R.layout.reservation_search_list_view, alarmInfoList));
	    lv.setItemsCanFocus(true);
	    
	    LinearLayout addAlarmBtn = (LinearLayout) findViewById(R.id.main_add_alarm_btn);
	    addAlarmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), ReservationSearchSetActivity.class);
				startActivity(i);
				
			}
		});
	
	}
}
