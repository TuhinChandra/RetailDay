package com.tcs.KingfisherDay.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.tcs.KingfisherDay.model.enums.Vote;
import com.tcs.KingfisherDay.model.key.EventResponseKey;

@Entity
@Table(name = "EVENT_RESPONSE")
@IdClass(EventResponseKey.class)
public class EventResponse {

	@Id
	@Column(name = "EVENT_ID", nullable = false)
	private int eventID;
	@Id
	@Column(name = "EMPLOYEE_EMAIL", nullable = false)
	private String employeeEmail;
	@Column(name = "VOTE", nullable = false)
	private Vote vote;
	@Column(name = "COMMENT", nullable = true)
	private String comment;
	@Column(name = "TIME_STAMP", nullable = false)
	private Timestamp timeStamp;

	public EventResponse() {

	}

	public EventResponse(int eventID, String employeeEmail, Vote vote, String comment, Timestamp timeStamp) {
		super();
		this.eventID = eventID;
		this.employeeEmail = employeeEmail;
		this.vote = vote;
		this.comment = comment;
		this.timeStamp = timeStamp;
	}

	public EventResponse(int eventID, String emailID, Vote vote, Timestamp timeStamp) {
		this.eventID = eventID;
		this.employeeEmail = emailID;
		this.vote = vote;
		this.timeStamp = timeStamp;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public Vote getVote() {
		return vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

}
