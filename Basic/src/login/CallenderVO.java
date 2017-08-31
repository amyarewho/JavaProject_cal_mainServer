package login;

import java.io.Serializable;
import java.sql.Date;

public class CallenderVO implements Serializable{
	
	
	private String id;
	private String title;
	private String category;
	private String memo;
	private String startDay;
	private String endDay;
	private boolean allDay;
	private boolean inputTime;
	private int repeatTerm;
	private int repeatNum;
	
	
	public CallenderVO(){
		
	}
	public CallenderVO(
			String id,String title,String category,String memo,String startDay,String endDay,
			boolean allDay,boolean inputTime,int repeatTerm,int repeatNum){
		this.id = id;
		this.title = title;
		this.category = category;
		this.memo = memo;
		this.startDay = startDay;
		this.endDay = endDay;
		this.allDay = allDay;
		this.inputTime = inputTime;
		this.repeatTerm = repeatTerm;
		this.repeatNum = repeatNum;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public boolean isAllDay() {
		return allDay;
	}
	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}
	public boolean isInputTime() {
		return inputTime;
	}
	public void setInputTime(boolean inputTime) {
		this.inputTime = inputTime;
	}
	public int getRepeatTerm() {
		return repeatTerm;
	}
	public void setRepeatTerm(int repeatTerm) {
		this.repeatTerm = repeatTerm;
	}
	public int getRepeatNum() {
		return repeatNum;
	}
	public void setRepeatNum(int repeatNum) {
		this.repeatNum = repeatNum;
	}
	
	@Override
	public String toString() {
		return "CallenderVO [id=" + id + ", title=" + title + ", category=" + category + ", memo=" + memo
				+ ", startDay=" + startDay + ", endDay=" + endDay + ", allDay=" + allDay + ", inputTime=" + inputTime
				+ ", repeatTerm=" + repeatTerm + ", repeatNum=" + repeatNum + "]";
	}
	
	
	

}
