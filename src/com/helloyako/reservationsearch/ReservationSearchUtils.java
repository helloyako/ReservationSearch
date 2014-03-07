package com.helloyako.reservationsearch;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ReservationSearchUtils {
	static public PendingIntent getPendingIntentForAlarmReceiver(Context context, String query, int index){
		Intent intent = new Intent(context, AlarmReceiver.class);
		Bundle bundle = new Bundle();
		bundle.putString("query", query);
		bundle.putInt("index", index);
		intent.putExtras(bundle);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, index , intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		return pendingIntent;
	}
}
