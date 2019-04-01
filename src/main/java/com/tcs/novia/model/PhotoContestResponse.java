package com.tcs.novia.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.tcs.novia.model.enums.Vote;
import com.tcs.novia.model.key.PhotoContestResponseKey;

@Entity
@Table(name = "PHOTOGRAPHY_CONTEST_RESPONSE")
@IdClass(PhotoContestResponseKey.class)
public class PhotoContestResponse {

	@Id
	@Column(name = "image_id", nullable = false)
	private int imageId;
	@Id
	@Column(name = "EMP_ID", nullable = false)
	private long employeeID;
	@Column(name = "VOTE", nullable = false)
	private Vote vote;
	@Column(name = "COMMENT", nullable = true)
	private String comment;
	@Column(name = "TIME_STAMP", nullable = false)
	private Timestamp timeStamp;

	public PhotoContestResponse() {

	}

	public PhotoContestResponse(final int imageId, final long employeeID, final Vote vote, final Timestamp timeStamp) {
		super();
		this.imageId = imageId;
		this.employeeID = employeeID;
		this.vote = vote;
		this.timeStamp = timeStamp;
	}

	public PhotoContestResponse(final int imageId, final long employeeID, final Vote vote, final String comment,
			final Timestamp timeStamp) {
		super();
		this.imageId = imageId;
		this.employeeID = employeeID;
		this.vote = vote;
		this.comment = comment;
		this.timeStamp = timeStamp;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(final int imageId) {
		this.imageId = imageId;
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
