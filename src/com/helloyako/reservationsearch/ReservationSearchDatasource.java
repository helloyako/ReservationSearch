package com.helloyako.reservationsearch;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReservationSearchDatasource {
	private SQLiteDatabase database;
	private ReservationSearchSQLiteHelper dbHelper;
	private String[] allColums = {};
	
	public ReservationSearchDatasource(Context context){
		dbHelper = new ReservationSearchSQLiteHelper(context);
	}
	
	public void open() {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public List<AlarmInfo> getAllAlarmInfo(){
		List<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
		Cursor cursor = null;
		
		cursor = database.query(ReservationSearchSQLiteHelper.DB_TABLE_NAME, allColums, null, null, null, null, null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			AlarmInfo alarmInfo = cursorToAlarmInfo(cursor);
			alarmInfoList.add(alarmInfo);
			cursor.moveToNext();
		}
		closeCursor(cursor);
		return alarmInfoList;
	}
	
	private void closeCursor(Cursor cursor){
		if(cursor != null){
			cursor.close();
		}
	}
	
	private AlarmInfo cursorToAlarmInfo(Cursor cursor){
		AlarmInfo alarmInfo = new AlarmInfo();
		int idIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper._INDEX);
		int alarmDateIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper.ALARM_DATE);
		int queryIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper.QUERY);
		int isActivationIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper.IS_ACTIVATION);
		
		alarmInfo.setId(cursor.getLong(idIndex));
		alarmInfo.setAlarmDate(cursor.getString(alarmDateIndex));
		alarmInfo.setQuery(cursor.getString(queryIndex));
		alarmInfo.setActivation(cursor.getInt(isActivationIndex) > 0);
		
		return alarmInfo;
	}

}
