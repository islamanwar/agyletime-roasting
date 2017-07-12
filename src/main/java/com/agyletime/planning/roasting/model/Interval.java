package com.agyletime.planning.roasting.model;

public class Interval {

	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;

	public static Interval newInstance(int startHour, int startMinute, int endHour, int endMinute) {
		Interval interval = new Interval();

		interval.startHour = startHour;
		interval.startMinute = startMinute;
		interval.endHour = endHour;
		interval.endMinute = endMinute;

		return interval;
	}

	public int getStartHour() {
		return startHour;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public int getEndHour() {
		return endHour;
	}

	public int getEndMinute() {
		return endMinute;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}

	public boolean contains(Interval interval2) {
		return (this.startHour < interval2.startHour
				|| (this.startHour == interval2.startHour && this.startMinute <= interval2.startMinute))
				&& (this.endHour > interval2.endHour
						|| (this.endHour == interval2.endHour && this.endMinute >= interval2.endMinute));
	}

	public boolean contains(int hour, int minute) {
		boolean startsBefore = this.startHour < hour || (this.startHour == hour && this.startMinute < minute);
		boolean endsAfter = this.endHour > hour || (this.endHour == hour && this.endMinute > minute);
		return startsBefore && endsAfter;
	}

	/**
	 * Check if an interval starts or ends during another interval
	 * 
	 * @param interval2
	 * @return
	 */
	public boolean intersects(Interval interval2) {

		return contains(interval2.getStartHour(), interval2.getStartMinute())
				|| contains(interval2.getEndHour(), interval2.getEndMinute());
	}

	public boolean startsBefore(Interval interval2) {
		return (this.startHour < interval2.startHour)
				|| (this.startHour == interval2.startHour && this.startMinute < interval2.startMinute);
	}

	public boolean endsAfter(Interval interval2) {
		return (this.endHour > interval2.endHour)
				|| (this.endHour == interval2.endHour && this.endMinute > interval2.endMinute);
	}

	public boolean endsBefore(Interval interval2) {
		return (this.endHour < interval2.endHour)
				|| (this.endHour == interval2.endHour && this.endMinute <= interval2.endMinute);
	}

	@Override
	public String toString() {
		return "Interval [startHour=" + startHour + ", startMinute=" + startMinute + ", endHour=" + endHour
				+ ", endMinute=" + endMinute + "]";
	}

}
