package com.helloyako.reservationsearch;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class ReservationSearchActivity extends Activity implements OnDateChangedListener,OnTimeChangedListener {
	private GregorianCalendar mCalender;
	private DatePicker mDate;
	private TimePicker mTime;
	
	private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getString(R.string.TAG);
        
        mCalender = new GregorianCalendar();
        
        Log.d(TAG,mCalender.getTime().toString());
        
        setContentView(R.layout.activity_reservation_search);
        
        Button setBtn = (Button) findViewById(R.id.setbtn);
        setBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setAlarm();
			}
		});
        
        mDate = (DatePicker) findViewById(R.id.datePicker1);
        mDate.init(mCalender.get(Calendar.YEAR), mCalender.get(Calendar.MONTH), mCalender.get(Calendar.DAY_OF_MONTH), this);
        
        mTime = (TimePicker) findViewById(R.id.timePicker1);
        mTime.setCurrentHour(mCalender.get(Calendar.HOUR_OF_DAY));
        mTime.setCurrentMinute(mCalender.get(Calendar.MINUTE));
        mTime.setOnTimeChangedListener(this);
        
    }

    private void setAlarm() {
    	CheckBox cb = (CheckBox) findViewById(R.id.checkBox1);
    	if(!cb.isChecked()){
    		long notWtime = System.currentTimeMillis();
    		if(notWtime >= mCalender.getTimeInMillis()){
    			Toast.makeText(ReservationSearchActivity.this, "입력한 날짜는 현재 날짜보다 이전 입니다", Toast.LENGTH_SHORT).show();
    			return;
    		}
    	}
    	
    	Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
    	EditText et = (EditText) findViewById(R.id.editText1);
    	String query = et.getText().toString();
    	if(query.equals("")){
    		Toast.makeText(ReservationSearchActivity.this,"검색어를 입력해주세요", Toast.LENGTH_LONG).show();
    		return;
    	}
    	intent.putExtra("query",query);
    	PendingIntent sender = PendingIntent.getBroadcast(ReservationSearchActivity.this, 0, intent, 0);
    	
    	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
    	if(cb.isChecked()){
    		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000 , 0, sender);
    	} else {
    		am.setRepeating(AlarmManager.RTC_WAKEUP, mCalender.getTimeInMillis(), 0, sender);
    	}
    	Toast.makeText(ReservationSearchActivity.this,mCalender.getTime().toString() + " 알람설정", Toast.LENGTH_LONG).show();
    	Log.d(TAG,"Set Query : "+ query);
    }

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mCalender.set(year, monthOfYear,dayOfMonth,mTime.getCurrentHour(),mTime.getCurrentMinute());
		Log.d(TAG,"onDateChanged : "+mCalender.getTime().toString());
	}
	
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		mCalender.set(mDate.getYear(), mDate.getMonth(),mDate.getDayOfMonth(),hourOfDay,minute);
		Log.d(TAG,"onTimeChanged : "+mCalender.getTime().toString());
	}

}
