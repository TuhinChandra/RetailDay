package com.tcs.KingfisherDay.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.tcs.KingfisherDay.model.enums.Vote;
import com.tcs.KingfisherDay.model.key.PhotoContestResponseKey;

@Entity
@Table(name = "PHOTOGRAPHY_CONTEST_RESPONSE")
@IdClass(PhotoContestResponseKey.class)
public class PhotoContestResponse {

	@Id
	@Column(name = "image_id", nullable = false)
	private int imageId;
	@Id
	@Column(name = "EMPLOYEE_EMAIL", nullable = false)
	private String employeeEmail;
	@Column(name = "VOTE", nullable = false)
	private Vote vote;
	@Column(name = "COMMENT", nullable = true)
	private String comment;
	@Column(name = "TIME_STAMP", nullable = false)
	private Timestamp timeStamp;

	public PhotoContestResponse() {

	}

	public PhotoContestResponse(int imageId, String employeeEmail, Vote vote, Timestamp timeStamp) {
		super();
		this.imageId = imageId;
		this.employeeEmail = employeeEmail;
		this.vote = vote;
		this.timeStamp = timeStamp;
	}

	public PhotoContestResponse(int imageId, String employeeEmail, Vote vote, String comment, Timestamp timeStamp) {
		super();
		this.imageId = imageId;
		this.employeeEmail = employeeEmail;
		this.vote = vote;
		this.comment = comment;
		this.timeStamp = timeStamp;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
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
