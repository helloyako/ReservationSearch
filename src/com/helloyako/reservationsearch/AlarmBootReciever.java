package com.helloyako.reservationsearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBootReciever extends BroadcastReceiver {
	private ReservationSearchDatasource dataSource;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			registerAlarms(context);
		}
	}

	private void registerAlarms(Context context) {
		dataSource = new ReservationSearchDatasource(context);
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.set(Calendar.SECOND, 0);
		gregorianCalendar.set(Calendar.MILLISECOND, 0);
		dataSource.open();
		ArrayList<AlarmInfo> alarmInfoList = (ArrayList<AlarmInfo>) dataSource
				.getAllAlarmInfo();
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		for (AlarmInfo alarmInfo : alarmInfoList) {
			String query = alarmInfo.getQuery();
			int index = alarmInfo.getIndex();
			gregorianCalendar.set(alarmInfo.getYear(), alarmInfo.getMonth(),
					alarmInfo.getDayOfMonth(), alarmInfo.getHour(),
					alarmInfo.getMin());

			PendingIntent sender = ReservationSearchCommon
					.getPendingIntentForAlarmReceiver(context, query, index);
			
			am.setRepeating(AlarmManager.RTC_WAKEUP,
					gregorianCalendar.getTimeInMillis(), 0, sender);
		}

		dataSource.close();
	}

}
