package com.helloyako.reservationsearch;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TimePicker.OnTimeChangedListener;

public class ReservationSearchSetActivity extends Activity implements OnTimeChangedListener{
	private GregorianCalendar mCalendar;
	private TimePicker mTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.reservation_search_set);
	    mCalendar = new GregorianCalendar();
	    mCalendar.set(Calendar.SECOND, 0);
	    mCalendar.set(Calendar.MILLISECOND, 0);
	    
	    mTime = (TimePicker) findViewById(R.id.timePicker);
        mTime.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mTime.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        mTime.setOnTimeChangedListener(this);
	    
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
    	Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
    	EditText et = (EditText) findViewById(R.id.queryEditText);
    	String query = et.getText().toString();
    	if(query.equals("")){
    		Toast.makeText(ReservationSearchSetActivity.this,"검색어를 입력해주세요", Toast.LENGTH_LONG).show();
    		return;
    	}
    	intent.putExtra("query",query);
    	PendingIntent sender = PendingIntent.getBroadcast(ReservationSearchSetActivity.this, 0, intent, 0);
    	
    	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
    	long calendarTimeInMillis = mCalendar.getTimeInMillis();
    	long timeDiffInMillis = calendarTimeInMillis - System.currentTimeMillis();
    	long remainderSec = timeDiffInMillis / 1000;
    	long remainderMin = remainderSec / 60;
    	long remainderHour = remainderMin / 60;
    	
//    	am.setRepeating(AlarmManager.RTC_WAKEUP, calendarTimeInMillis, 0, sender);
    	
    	Toast.makeText(ReservationSearchSetActivity.this,remainderHour + "시간" + remainderMin + "분" + remainderSec + "초 후에 알람이 울립니다.", Toast.LENGTH_LONG).show();
//    	finish();
    }

	@Override
	public void onTimeChanged(TimePicker arg0, int hourOfDay, int minute) {
		mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mCalendar.set(Calendar.MINUTE, minute);
	}

}
