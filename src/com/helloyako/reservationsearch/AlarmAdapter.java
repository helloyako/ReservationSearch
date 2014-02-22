package com.helloyako.reservationsearch;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AlarmAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private ArrayList<AlarmInfo> alarmInfoList;
	private int layout;
	
	public AlarmAdapter(Context context, int layout, ArrayList<AlarmInfo> alarmInfoList){
		Log.d("reservation","alarmadapter constro");
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.alarmInfoList = alarmInfoList;
		this.layout = layout;
	}

	@Override
	public int getCount() {
		return this.alarmInfoList.size();
	}

	@Override
	public Object getItem(int pos) {
		return this.alarmInfoList.get(pos).getQuery();
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = this.inflater.inflate(this.layout, parent,false);
			Log.d("reservation","convertView");
		}
		AlarmInfo alarmInfo = this.alarmInfoList.get(pos);
		
		TextView alarmDate = (TextView) convertView.findViewById(R.id.alarm_date);
		alarmDate.setText(alarmInfo.getAlarmDate());
		
		TextView query = (TextView) convertView.findViewById(R.id.query);
		query.setText(alarmInfo.getQuery());
		
//		TextView week = (TextView) convertView.findViewById(R.id.week);
//		week.setText(alarmInfo.get);
		
		CheckBox alarmActivation = (CheckBox) convertView.findViewById(R.id.alarm_activation);
		alarmActivation.setFocusable(false);
		alarmActivation.setChecked(alarmInfo.isActivation());
		
		return convertView;
	}
	
	


}
