package com.helloyako.reservationsearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import android.widget.AdapterView.OnItemClickListener;
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
		lv.setAdapter(new AlarmAdapter(this,
				R.layout.reservation_search_list_view, alarmInfoList));

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final AlarmInfo alarmInfo = alarmInfoList.get(position);
				builder.setTitle(alarmInfo.getQuery()).setItems(R.array.dialog,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									reservationSearchSetActivityStart(alarmInfo);
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
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long id) {
				AlarmInfo alarmInfo = alarmInfoList.get(position);
				reservationSearchSetActivityStart(alarmInfo);
			}
		});

		LinearLayout addAlarmBtn = (LinearLayout) findViewById(R.id.main_add_alarm_btn);
		addAlarmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				AlarmInfo alarmInfo = new AlarmInfo(
						dataSource.getLastIndex() + 1, gregorianCalendar
								.get(Calendar.YEAR), gregorianCalendar
								.get(Calendar.MONTH), gregorianCalendar
								.get(Calendar.DAY_OF_MONTH), gregorianCalendar
								.get(Calendar.HOUR_OF_DAY), gregorianCalendar
								.get(Calendar.MINUTE), "", false, false);

				reservationSearchSetActivityStart(alarmInfo);
			}
		});
	}

	private void reservationSearchSetActivityStart(AlarmInfo alarmInfo) {
		Intent i = new Intent(getApplicationContext(),
				ReservationSearchSetActivity.class);
		Bundle extras = new Bundle();
		extras.putInt(ReservationSearchSQLiteHelper._INDEX, alarmInfo.getIndex());
		extras.putInt(ReservationSearchSQLiteHelper.ALARM_YEAR, alarmInfo.getYear());
		extras.putInt(ReservationSearchSQLiteHelper.ALARM_MONTH, alarmInfo.getMonth());
		extras.putInt(ReservationSearchSQLiteHelper.ALARM_DAY_OF_MONTH, alarmInfo.getDayOfMonth());
		extras.putInt(ReservationSearchSQLiteHelper.ALARM_HOUR, alarmInfo.getHour());
		extras.putInt(ReservationSearchSQLiteHelper.ALARM_MIN, alarmInfo.getMin());
		extras.putString(ReservationSearchSQLiteHelper.QUERY, alarmInfo.getQuery());
		extras.putBoolean(ReservationSearchSQLiteHelper.IS_ACTIVATION, alarmInfo.isActivation());
		extras.putBoolean(ReservationSearchSQLiteHelper.IS_ACTIVATION, alarmInfo.isRepeat());
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

	private void refreshListView() {
		alarmInfoList = (ArrayList<AlarmInfo>) dataSource.getAllAlarmInfo();
		lv.setAdapter(new AlarmAdapter(this,
				R.layout.reservation_search_list_view, alarmInfoList));
	}

	private void deleteAlarm(AlarmInfo alarmInfo) {
		int alarmIndex = alarmInfo.getIndex();
		String alarmQuery = alarmInfo.getQuery();
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

		PendingIntent operation = ReservationSearchCommon
				.getPendingIntentForAlarmReceiver(getApplicationContext(),
						alarmQuery, alarmIndex);

		am.cancel(operation);
		dataSource.deleteAlarm(alarmInfo.getIndex());
	}
}
