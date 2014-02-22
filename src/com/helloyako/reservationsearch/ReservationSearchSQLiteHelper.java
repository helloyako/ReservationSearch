package com.helloyako.reservationsearch;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReservationSearchSQLiteHelper extends SQLiteOpenHelper {

	public static final String DB_TABLE_NAME = "reservationsearch";
	public static final String _INDEX = "_index";
	public static final String ALARM_DATE = "alarm_date";
	public static final String QUERY = "_query";
	public static final String IS_ACTIVATION = "_is_activation";
	
	private static final String DB_NAME = "ReservastionSearchDB";
	private static final int DB_VERSION = 1;
	private final String CREATE_STATEMENT = StringUtils.join(
			"create table ",
			DB_TABLE_NAME,
			"(",
			_INDEX," INTEGER PRIMARY KEY AUTOINCREMENT, ",
			ALARM_DATE," DATETIME, ",
			QUERY," TEXT, ",
			IS_ACTIVATION, " BOOLEAN",
			");"
			);
	
	public ReservationSearchSQLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_STATEMENT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
		onCreate(db);
	}
}
