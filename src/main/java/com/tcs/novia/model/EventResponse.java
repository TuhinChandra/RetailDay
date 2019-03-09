package com.tcs.novia.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.tcs.novia.model.enums.Vote;
import com.tcs.novia.model.key.EventResponseKey;

@Entity
@Table(name = "EVENT_RESPONSE")
@IdClass(EventResponseKey.class)
public class EventResponse {

	@Id
	@Column(name = "EVENT_ID", nullable = false)
	private int eventID;
	@Id
	@Column(name = "EMP_ID", nullable = false)
	private long employeeID;
	@Column(name = "VOTE", nullable = false)
	private Vote vote;
	@Column(name = "COMMENT", nullable = true)
	private String comment;
	@Column(name = "TIME_STAMP", nullable = false)
	private Timestamp timeStamp;

	public EventResponse() {

	}

	public EventResponse(final int eventID, final long employeeID, final Vote vote, final String comment,
			final Timestamp timeStamp) {
		super();
		this.eventID = eventID;
		this.employeeID = employeeID;
		this.vote = vote;
		this.comment = comment;
		this.timeStamp = timeStamp;
	}

	public EventResponse(final int eventID, final long employeeID, final Vote vote, final Timestamp timeStamp) {
		this.eventID = eventID;
		this.employeeID = employeeID;
		this.vote = vote;
		this.timeStamp = timeStamp;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(final int eventID) {
		this.eventID = eventID;
	}

	public long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(final long employeeID) {
		this.employeeID = employeeID;
	}

	public Vote getVote() {
		return vote;
	}

	public void setVote(final Vote vote) {
		this.vote = vote;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(final Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

}
