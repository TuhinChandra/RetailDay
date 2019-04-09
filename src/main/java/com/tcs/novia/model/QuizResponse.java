package com.tcs.novia.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.apache.commons.lang3.BooleanUtils;

import com.tcs.novia.model.key.QuizResponseKey;

@Entity
@Table(name = "QUIZ_RESPONSE")
@IdClass(QuizResponseKey.class)
public class QuizResponse {

	@Id
	@Column(name = "QUESTION_ID", nullable = false)
	private String questionID;
	@Id
	@Column(name = "EMP_ID", nullable = false)
	private long employeeID;
	@Column(name = "OPTION_SELECTED", nullable = false)
	private String option;
	@Column(name = "TIME_STAMP", nullable = false)
	private LocalDateTime timeStamp;

	private Boolean alreadyAnswered;

	public QuizResponse() {

	}

	public QuizResponse(final String questionID, final long employeeID, final String option,
			final LocalDateTime timeStamp) {
		super();
		this.questionID = questionID;
		this.employeeID = employeeID;
		this.option = option;
		this.timeStamp = timeStamp;
	}

	public String getQuestionID() {
		return questionID;
	}

	public void setQuestionID(final String questionID) {
		this.questionID = questionID;
	}

	public long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(final long employeeID) {
		this.employeeID = employeeID;
	}

	public String getOption() {
		return option;
	}

	public void setOption(final String option) {
		this.option = option;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(final LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "QuizResponse [questionID=" + questionID + ", employeeID=" + employeeID + ", option=" + option
				+ ", timeStamp=" + timeStamp + "]";
	}

	public boolean isAlreadyAnswered() {
		return BooleanUtils.toBoolean(alreadyAnswered);
	}

	public void setAlreadyAnswered(final Boolean alreadyAnswered) {
		this.alreadyAnswered = alreadyAnswered;
	}

}
