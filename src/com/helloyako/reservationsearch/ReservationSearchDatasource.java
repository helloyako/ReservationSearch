package com.helloyako.reservationsearch;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
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
	
	public void createAlarm(int year, int month, int dayOfMonth, int hour, int min, String query){
		ContentValues values = new ContentValues();
		values.put(ReservationSearchSQLiteHelper.ALARM_YEAR, year);
		values.put(ReservationSearchSQLiteHelper.ALARM_MONTH, month);
		values.put(ReservationSearchSQLiteHelper.ALARM_DAY_OF_MONTH, dayOfMonth);
		values.put(ReservationSearchSQLiteHelper.ALARM_HOUR, hour);
		values.put(ReservationSearchSQLiteHelper.ALARM_MIN, min);
		values.put(ReservationSearchSQLiteHelper.QUERY, query);
		values.put(ReservationSearchSQLiteHelper.IS_ACTIVATION, 1);
		database.insert(ReservationSearchSQLiteHelper.DB_TABLE_NAME, null, values);
	}
	
	public void deleteAlarm(int index){
		database.delete(ReservationSearchSQLiteHelper.DB_TABLE_NAME, ReservationSearchSQLiteHelper._INDEX + "=" + index, null);
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
	
	public int getLastIndex(){
		String[] indexColum = {ReservationSearchSQLiteHelper._INDEX};
		String orderBy = ReservationSearchSQLiteHelper._INDEX + " DESC";
		Cursor cursor = database.query(ReservationSearchSQLiteHelper.DB_TABLE_NAME, indexColum, null, null, null, null, orderBy,"1");
		cursor.moveToFirst();
		int lastIndex = 0;
		if(!cursor.isAfterLast()){
			int indexIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper._INDEX);
			lastIndex = cursor.getInt(indexIndex);
		}
		closeCursor(cursor);
		return lastIndex;
	}
	
	public AlarmInfo getAlarmInfo(int index){
		Cursor cursor = null;
		String selection = ReservationSearchSQLiteHelper._INDEX + "=?";
		String[] selectionArgs = {Integer.toString(index)};
		cursor = database.query(ReservationSearchSQLiteHelper.DB_TABLE_NAME, allColums, selection, selectionArgs, null, null, null);
		cursor.moveToFirst();
		AlarmInfo alarmInfo = null;
		if(!cursor.isAfterLast()){
			alarmInfo = cursorToAlarmInfo(cursor);
		}
		
		closeCursor(cursor);
		
		return alarmInfo;
	}
	
	private void closeCursor(Cursor cursor){
		if(cursor != null){
			cursor.close();
		}
	}
	
	private AlarmInfo cursorToAlarmInfo(Cursor cursor){
		AlarmInfo alarmInfo = new AlarmInfo();
		int indexIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper._INDEX);
		int alarmYearIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper.ALARM_YEAR);
		int alarmMonthIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper.ALARM_MONTH);
		int alarmDayOfMonthIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper.ALARM_DAY_OF_MONTH);
		int alarmHourIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper.ALARM_HOUR);
		int alarmMinIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper.ALARM_MIN);
		int queryIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper.QUERY);
		int isActivationIndex = cursor.getColumnIndex(ReservationSearchSQLiteHelper.IS_ACTIVATION);
		
		alarmInfo.setIndex(cursor.getInt(indexIndex));
		alarmInfo.setYear(cursor.getInt(alarmYearIndex));
		alarmInfo.setMonth(cursor.getInt(alarmMonthIndex));
		alarmInfo.setDayOfMonth(cursor.getInt(alarmDayOfMonthIndex));
		alarmInfo.setHour(cursor.getInt(alarmHourIndex));
		alarmInfo.setMin(cursor.getInt(alarmMinIndex));
		alarmInfo.setQuery(cursor.getString(queryIndex));
		alarmInfo.setActivation(cursor.getInt(isActivationIndex) > 0);
		alarmInfo.setRepeat(false);
		
		return alarmInfo;
	}

}
