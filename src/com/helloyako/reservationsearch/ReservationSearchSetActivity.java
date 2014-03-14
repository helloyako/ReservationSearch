package com.helloyako.reservationsearch;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TimePicker.OnTimeChangedListener;

public class ReservationSearchSetActivity extends Activity implements
		OnTimeChangedListener, OnDateChangedListener {
	private GregorianCalendar mCalendar;
	private TimePicker mTime;
	private DatePicker mDate;
	private EditText mEditText;
	private ReservationSearchDatasource dataSource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_search_set);
		
		mEditText = (EditText) findViewById(R.id.queryEditText);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		final int index = bundle.getInt(ReservationSearchSQLiteHelper._INDEX);
		int year = bundle.getInt(ReservationSearchSQLiteHelper.ALARM_YEAR);
		int month = bundle.getInt(ReservationSearchSQLiteHelper.ALARM_MONTH);
		int dayOfMonth = bundle
				.getInt(ReservationSearchSQLiteHelper.ALARM_DAY_OF_MONTH);
		int hour = bundle.getInt(ReservationSearchSQLiteHelper.ALARM_HOUR);
		int min = bundle.getInt(ReservationSearchSQLiteHelper.ALARM_MIN);
		String query = bundle.getString(ReservationSearchSQLiteHelper.QUERY);
		
		mEditText.setText(query);

		mCalendar = ReservationSearchCommon.getGregorianCalendar(year, month,
				dayOfMonth, hour, min);

		mDate = (DatePicker) findViewById(R.id.datePicker);
		mDate.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH), this);

		mTime = (TimePicker) findViewById(R.id.timePicker);
		mTime.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
		mTime.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
		mTime.setOnTimeChangedListener(this);

		dataSource = new ReservationSearchDatasource(this);
		dataSource.open();

		Button saveBtn = (Button) findViewById(R.id.set_save_btn);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setAlarm(index);
			}
		});

		Button cancelBtn = (Button) findViewById(R.id.set_cancel_btn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void setAlarm(int index) {
		String query = mEditText.getText().toString();
		if (StringUtils.isBlank(query)) {
			Toast.makeText(ReservationSearchSetActivity.this,
					getString(R.string.query_warning), Toast.LENGTH_LONG)
					.show();
			return;
		}
		AlarmInfo alarmInfo = dataSource.getAlarmInfo(index);
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
		int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int min = mCalendar.get(Calendar.MINUTE);
		if (alarmInfo == null) {
			dataSource.createAlarm(year,month,dayOfMonth,hour,min, query);
		} else {
			dataSource.updateAlarm(index, year, month, dayOfMonth, hour, min, query);
		}

		PendingIntent sender = ReservationSearchCommon
				.getPendingIntentForAlarmReceiver(getApplicationContext(),
						query, index);
		
		long calendarTimeInMillis = mCalendar.getTimeInMillis();
		long timeDiffInMillis = calendarTimeInMillis
				- System.currentTimeMillis();
		long remainderSec = timeDiffInMillis / 1000;
		long remainderMin = remainderSec / 60;
		long remainderHour = remainderMin / 60;

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendarTimeInMillis, 0,
				sender);

		Toast.makeText(
				ReservationSearchSetActivity.this,
				remainderHour + "시간" + remainderMin + "분" + remainderSec
						+ "초 후에 알람이 울립니다.", Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public void onTimeChanged(TimePicker arg0, int hourOfDay, int minute) {
		mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mCalendar.set(Calendar.MINUTE, minute);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mCalendar.set(year, monthOfYear, dayOfMonth, mTime.getCurrentHour(),
				mTime.getCurrentMinute());
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}

	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}
}
