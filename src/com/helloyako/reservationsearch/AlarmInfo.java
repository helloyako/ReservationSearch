package com.helloyako.reservationsearch;

public class AlarmInfo {
	private int index;
	private int year;
	private int month;
	private int dayOfMonth;
	private int hour;
	private int min;
	private String query;
	private boolean isRepeat;
	private boolean isActivation;
	
	public AlarmInfo(int index, int year, int month, int dayOfMonth, int hour,
			int min, String query, boolean isRepeat, boolean isActivation) {
		super();
		this.index = index;
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
		this.hour = hour;
		this.min = min;
		this.query = query;
		this.isRepeat = isRepeat;
		this.isActivation = isActivation;
	}

	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMin() {
		return min;
	}

	public String getQuery() {
		return query;
	}

	public boolean isRepeat() {
		return isRepeat;
	}

	public boolean isActivation() {
		return isActivation;
	}

	public int getIndex() {
		return index;
	}
}
