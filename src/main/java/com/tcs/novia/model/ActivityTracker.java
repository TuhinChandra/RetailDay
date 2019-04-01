package com.tcs.novia.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "ActivityTracker")
@Table(name = "ACTIVITY_TRACKER")
public class ActivityTracker {

	@Id
	@GeneratedValue
	private int id;
	@Column(name = "ACTIVITY_NAME", nullable = false)
	private String activityName;
	@Column(name = "DATE_TIME_STAMP", nullable = false)
	private LocalDateTime dateTimeStamp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID", nullable = false)
	@JsonIgnore
	private Employee employee;

	public ActivityTracker() {
		super();
	}

	public ActivityTracker(String activityName, LocalDateTime dateTimeStamp, Employee employee) {
		super();
		this.activityName = activityName;
		this.dateTimeStamp = dateTimeStamp;
		this.employee = employee;
	}

	public int getId() {
		return id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public LocalDateTime getDateTimeStamp() {
		return dateTimeStamp;
	}

	public void setDateTimeStamp(LocalDateTime dateTimeStamp) {
		this.dateTimeStamp = dateTimeStamp;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}


}
