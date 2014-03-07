package com.helloyako.reservationsearch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class AlarmReceiver extends BroadcastReceiver {
	private ReservationSearchDatasource dataSource;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		dataSource = new ReservationSearchDatasource(context);
		dataSource.open();
		
		Bundle bundle = intent.getExtras();
		String query = bundle.getString("query");
		int index = bundle.getInt("index");
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WakeLock screenWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, context.getString(R.string.TAG));
		if(screenWakeLock != null){
			screenWakeLock.acquire();
		}
		
		showNotification(context, R.drawable.rs_icon, context.getString(R.string.app_name), query, index);
		if(screenWakeLock != null){
			screenWakeLock.release();
			screenWakeLock = null;
		}
		refreshAlarmInfo(index);
		dataSource.close();
	}
	
	private void refreshAlarmInfo(int index){
		AlarmInfo alarmInfo = dataSource.getAlarmInfo(index);
		if(alarmInfo.isRepeat()){
			//TODO register next alarm
		} else {
			dataSource.deleteAlarm(index);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void showNotification(Context context, int statusBarIconID, String statusBarText, String detailedText, int index){
		String uriString = "http://m.search.naver.com/search.naver?where=m&sm=mtp_hty&query="+detailedText;
		Intent searchIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(uriString));
		
		PendingIntent theappIntent = PendingIntent.getActivity(context, index, searchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		CharSequence from = context.getString(R.string.app_name);
		CharSequence message = detailedText;
		
		Notification notif = new Notification(statusBarIconID, statusBarText, System.currentTimeMillis());
		notif.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
		notif.defaults = Notification.DEFAULT_ALL;
		notif.ledARGB = Color.CYAN;
		notif.setLatestEventInfo(context, from, message, theappIntent);
		
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		nm.notify(index, notif);
	}
}
