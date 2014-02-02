package com.helloyako.reservationsearch;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ReservationSearchListActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.reservation_search_list);
	    
	    ListView lv1 = (ListView) findViewById(R.id.listView1);
	    lv1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,new String[]{getString(R.string.addAlarm)}));
	    
	    ListView lv2 = (ListView) findViewById(R.id.listView2);
	    ArrayList<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
	    AlarmInfo ai1 = new AlarmInfo("08:00", "검색어1", false, false);
	    AlarmInfo ai2 = new AlarmInfo("09:00", "검색어2", false, false);
	    AlarmInfo ai3 = new AlarmInfo("10:00", "검색어3", false, false);
	    AlarmInfo ai4 = new AlarmInfo("11:00", "검색어4", false, false);
	    AlarmInfo ai5 = new AlarmInfo("12:00", "검색어5", false, false);
	    AlarmInfo ai6 = new AlarmInfo("13:00", "검색어6", false, false);
	    AlarmInfo ai7 = new AlarmInfo("14:00", "검색어7", false, false);
	    AlarmInfo ai8 = new AlarmInfo("15:00", "검색어8", false, false);
	    AlarmInfo ai9 = new AlarmInfo("16:00", "검색어19", false, false);
	    
	    alarmInfoList.add(ai1);
	    alarmInfoList.add(ai2);
	    alarmInfoList.add(ai3);
	    alarmInfoList.add(ai4);
	    alarmInfoList.add(ai5);
	    alarmInfoList.add(ai6);
	    alarmInfoList.add(ai7);
	    alarmInfoList.add(ai8);
	    alarmInfoList.add(ai9);
	    Log.d("reservation","OnCreate");
	    lv2.setAdapter(new AlarmAdapter(this, R.layout.reservation_search_list_view, alarmInfoList));
	
	}

}
