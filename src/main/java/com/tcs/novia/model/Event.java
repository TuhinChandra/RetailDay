package com.tcs.novia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcs.novia.model.enums.EventState;

@Entity
@Table(name = "EVENT")
public class Event {
	@Id
	@GeneratedValue
	@Column(name = "event_id", nullable = false)
	private int eventID;
	@Column(name = "event_name", nullable = false)
	private String eventName;
	@Column(name = "event_date", nullable = true)
	private String eventDate;
	@Column(name = "event_details", nullable = false)
	private String details;
	@Column(name = "start", nullable = true)
	private String start;
	@Column(name = "finish", nullable = true)
	private String end;
	@JsonIgnore
	@Column(name = "state", nullable = true)
	private EventState state;
	@JsonIgnore
	@Column(name = "ppt_path", nullable = true)
	private String pptPath;
	@Column(name = "photo", nullable = true, length = 10485760)
	private String photo;
	@Column(name = "duration", nullable = true)
	private String duration;

	public Event() {

	}

	public Event(final int eventID, final String eventName, final String eventDate, final String details,
			final String start, final String end, final EventState state, final String pptPath, final String photo,
			final String duration) {
		this.eventID = eventID;
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.details = details;
		this.start = start;
		this.end = end;
		this.state = state;
		this.pptPath = pptPath;
		this.photo = photo;
		this.duration = duration;
	}
	public Event(final String eventName, final String eventDate, final String details,
			final String start, final String end, final String photo,
			final String duration,final EventState state) {
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.details = details;
		this.start = start;
		this.end = end;
		this.photo = photo;
		this.duration = duration;
		this.state=state;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(final int eventID) {
		this.eventID = eventID;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(final String eventDate) {
		this.eventDate = eventDate;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(final String details) {
		this.details = details;
	}

	public String getStart() {
		return start;
	}

	public void setStart(final String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(final String end) {
		this.end = end;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	public EventState getState() {
		return state;
	}

	public void setState(final EventState state) {
		this.state = state;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(final String eventName) {
		this.eventName = eventName;
	}

	public String getPptPath() {
		return pptPath;
	}

	public void setPptPath(final String pptPath) {
		this.pptPath = pptPath;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(final String duration) {
		this.duration = duration;
	}

}