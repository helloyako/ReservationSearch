package com.helloyako.reservationsearch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class AlarmReceiver extends BroadcastReceiver {

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		String query = intent.getStringExtra("query");
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WakeLock screenWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, context.getString(R.string.TAG));
		if(screenWakeLock != null){
			screenWakeLock.acquire();
		}
		
		showNotification(context, R.drawable.rs_icon, context.getString(R.string.app_name), query);
		if(screenWakeLock != null){
			screenWakeLock.release();
			screenWakeLock = null;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void showNotification(Context context, int statusBarIconID, String statusBarTextID, String detailedTextID){
		String uriString = "http://m.search.naver.com/search.naver?where=m&sm=mtp_hty&query="+detailedTextID;
		Intent searchIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(uriString));
		
		PendingIntent theappIntent = PendingIntent.getActivity(context, 0, searchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		CharSequence from = context.getString(R.string.app_name);
		CharSequence message = detailedTextID;
		
		Notification notif = new Notification(statusBarIconID, statusBarTextID, System.currentTimeMillis());
		notif.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
		notif.defaults = Notification.DEFAULT_ALL;
		notif.ledARGB = Color.CYAN;
		notif.setLatestEventInfo(context, from, message, theappIntent);
		
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		nm.notify(1234, notif);
	}

}
