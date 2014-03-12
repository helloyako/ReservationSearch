package com.helloyako.reservationsearch;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
	private ReservationSearchDatasource dataSource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_search_set);
		mCalendar = new GregorianCalendar();
		mCalendar.set(Calendar.SECOND, 0);
		mCalendar.set(Calendar.MILLISECOND, 0);

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
				setAlarm();
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

	private void setAlarm() {
		EditText et = (EditText) findViewById(R.id.queryEditText);
		String query = et.getText().toString();
		if (StringUtils.isBlank(query)) {
			Toast.makeText(ReservationSearchSetActivity.this, getString(R.string.query_warning),
					Toast.LENGTH_LONG).show();
			return;
		}

		dataSource.createAlarm(mCalendar.get(Calendar.YEAR),
				mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH),
				mCalendar.get(Calendar.HOUR_OF_DAY),
				mCalendar.get(Calendar.MINUTE), query);

		int index = dataSource.getLastIndex();

		PendingIntent sender = ReservationSearchCommon
				.getPendingIntentForAlarmReceiver(getApplicationContext(),
						query, index);

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		long calendarTimeInMillis = mCalendar.getTimeInMillis();
		long timeDiffInMillis = calendarTimeInMillis
				- System.currentTimeMillis();
		long remainderSec = timeDiffInMillis / 1000;
		long remainderMin = remainderSec / 60;
		long remainderHour = remainderMin / 60;

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
