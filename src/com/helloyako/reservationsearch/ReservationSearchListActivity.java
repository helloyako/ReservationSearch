package com.helloyako.reservationsearch;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

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

	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final AlarmInfo alarmInfo = alarmInfoList.get(position);
				builder.setTitle(alarmInfo.getQuery()).setItems(R.array.dialog, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which == 0){
							reservationSearchSetActivityStart(true);
						} else {
							deleteAlarm(alarmInfo);
							refreshListView();
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				return false;
			}
		});
	    
	    LinearLayout addAlarmBtn = (LinearLayout) findViewById(R.id.main_add_alarm_btn);
	    addAlarmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				reservationSearchSetActivityStart(true);
			}
		});
	}
	
	private void reservationSearchSetActivityStart(boolean isNew){
		Intent i = new Intent(getApplicationContext(), ReservationSearchSetActivity.class);
		Bundle extras = new Bundle();
		extras.putBoolean("new", true);
		i.putExtras(extras);
		startActivity(i);
	}
	
	@Override
	protected void onResume() {
		dataSource.open();
		refreshListView();
	    
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}
	
	private void refreshListView(){
		alarmInfoList = (ArrayList<AlarmInfo>) dataSource.getAllAlarmInfo();
	    lv.setAdapter(new AlarmAdapter(this, R.layout.reservation_search_list_view, alarmInfoList));
	}
	
	private void deleteAlarm(AlarmInfo alarmInfo){
		int alarmIndex = alarmInfo.getIndex();
		String alarmQuery = alarmInfo.getQuery();
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		PendingIntent operation = ReservationSearchUtils.getPendingIntentForAlarmReceiver(getApplicationContext(), alarmQuery, alarmIndex);
		
		am.cancel(operation);
		dataSource.deleteAlarm(alarmInfo.getIndex());
	}
}
