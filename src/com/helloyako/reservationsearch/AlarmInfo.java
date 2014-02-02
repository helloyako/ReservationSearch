package com.helloyako.reservationsearch;

public class AlarmInfo {
	private String alarmDate;
	private String query;
	private boolean isRepeat;
	private boolean isActivation;
	
	public AlarmInfo(String alarmDate, String query, boolean isRepeat,
			boolean isActivation) {
		super();
		this.alarmDate = alarmDate;
		this.query = query;
		this.isRepeat = isRepeat;
		this.isActivation = isActivation;
	}

	public String getAlarmDate() {
		return alarmDate;
	}

	public void setAlarmDate(String alarmDate) {
		this.alarmDate = alarmDate;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isRepeat() {
		return isRepeat;
	}

	public void setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
	}

	public boolean isActivation() {
		return isActivation;
	}

	public void setActivation(boolean isActivation) {
		this.isActivation = isActivation;
	}
}
