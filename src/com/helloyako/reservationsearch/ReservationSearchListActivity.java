package com.helloyako.reservationsearch;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ReservationSearchListActivity extends Activity {
	
	private ArrayList<AlarmInfo> alarmInfoList;
	private ReservationSearchDatasource dataSource;
	private ListView lv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.reservation_search_list);
	    
	    dataSource = new ReservationSearchDatasource(this);
	    dataSource.open();
	    
	    alarmInfoList = (ArrayList<AlarmInfo>) dataSource.getAllAlarmInfo();
	    
	    lv = (ListView) findViewById(R.id.listView);
	    lv.setAdapter(new AlarmAdapter(this, R.layout.reservation_search_list_view, alarmInfoList));
	    lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlarmInfo alarmInfo = alarmInfoList.get(position);
				dataSource.deleteAlarm(alarmInfo.getId());
				onResume();
				return false;
			}
		});
	    
	    LinearLayout addAlarmBtn = (LinearLayout) findViewById(R.id.main_add_alarm_btn);
	    addAlarmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), ReservationSearchSetActivity.class);
				
				startActivity(i);
			}
		});
	}
	
	@Override
	protected void onResume() {
		dataSource.open();
		alarmInfoList = (ArrayList<AlarmInfo>) dataSource.getAllAlarmInfo();
	    lv.setAdapter(new AlarmAdapter(this, R.layout.reservation_search_list_view, alarmInfoList));
	    
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}
}
