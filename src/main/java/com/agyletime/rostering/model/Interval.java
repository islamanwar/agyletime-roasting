package com.agyletime.rostering.model;

import java.util.Date;

public class Interval {

	private Date startTime;
	private Date endTime;

	public static Interval newInstance(Date startTime, Date endTime) {
		Interval interval = new Interval();

		interval.startTime = startTime;
		interval.endTime = endTime;

		return interval;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean contains(Interval interval2) {
		boolean startsBeforeOrWith = this.startTime.before(interval2.getStartTime()) || this.startTime.equals(interval2.getStartTime());
		boolean endsAfterOrWith = this.endTime.after(interval2.getEndTime()) || this.endTime.equals(interval2.getEndTime());
		
		return startsBeforeOrWith && endsAfterOrWith;

	}

	public boolean contains(Date time) {
		boolean startsBeforeOrWith = this.startTime.before(time) || this.startTime.equals(time);
		boolean endsAfterOrWith = this.endTime.after(time) || this.endTime.equals(time);
		return startsBeforeOrWith && endsAfterOrWith;
	}

	/**
	 * Check if an interval starts or ends during another interval
	 * 
	 * @param interval2
	 * @return
	 */
	public boolean intersects(Interval interval2) {

		return contains(interval2.getStartTime())
				|| contains(interval2.getEndTime());
	}

	@Override
	public String toString() {
		return "Interval [startTime=" + startTime + ", endTime=" + endTime + "]";
	}

	

}
