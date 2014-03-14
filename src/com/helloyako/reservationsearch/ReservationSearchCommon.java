package com.helloyako.reservationsearch;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ReservationSearchCommon {
	public static PendingIntent getPendingIntentForAlarmReceiver(Context context, String query, int index){
		Intent intent = new Intent(context, AlarmReceiver.class);
		Bundle bundle = new Bundle();
		bundle.putString("query", query);
		bundle.putInt("index", index);
		intent.putExtras(bundle);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, index , intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		return pendingIntent;
	}
	
	public static GregorianCalendar getGregorianCalendar(int year, int month, int dayOfMonth, int hour, int min){
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.set(Calendar.YEAR, year);
		gregorianCalendar.set(Calendar.MONTH, month);
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		gregorianCalendar.set(Calendar.HOUR_OF_DAY, hour);
		gregorianCalendar.set(Calendar.MINUTE, min);
		gregorianCalendar.set(Calendar.SECOND, 0);
		gregorianCalendar.set(Calendar.MILLISECOND, 0);
		
		return gregorianCalendar;
	}
}
