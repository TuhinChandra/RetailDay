package com.tcs.novia.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SurveyFeedback {

	@Id
	@Column(name = "EMP_ID", nullable = false)
	private long employeeID;

	@Column(name = "QUESTION_1", nullable = false)
	private int question1;
	@Column(name = "ANSWER_1", nullable = false)
	private String answer1;

	@Column(name = "QUESTION_2", nullable = false)
	private int question2;
	@Column(name = "ANSWER_2", nullable = false)
	private String answer2;

	@Column(name = "QUESTION_3", nullable = false)
	private int question3;
	@Column(name = "ANSWER_3", nullable = false)
	private String answer3;

	@Column(name = "QUESTION_4", nullable = false)
	private int question4;
	@Column(name = "ANSWER_4", nullable = false)
	private String answer4;
	
	@Column(name = "QUESTION_5", nullable = false)
	private int question5;
	@Column(name = "ANSWER_5", nullable = false)
	private String answer5;

	@Column(name = "TIME_STAMP", nullable = false)
	private LocalDateTime timeStamp;
	@Column(name = "FEEDBACK", nullable = true, length = 2000)
	private String feedbackComment;

	public SurveyFeedback() {

	}

	public SurveyFeedback(long employeeID, int question1, String answer1, int question2, String answer2, int question3,
			String answer3, int question4, String answer4, int question5, String answer5, LocalDateTime timeStamp, String feedbackComment) {
		super();
		this.employeeID = employeeID;
		this.question1 = question1;
		this.answer1 = answer1;
		this.question2 = question2;
		this.answer2 = answer2;
		this.question3 = question3;
		this.answer3 = answer3;
		this.question4 = question4;
		this.answer4 = answer4;
		this.question5 = question5;
		this.answer5 = answer5;
		this.timeStamp = timeStamp;
		this.feedbackComment = feedbackComment;
	}

	public long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(long employeeID) {
		this.employeeID = employeeID;
	}

	public int getQuestion1() {
		return question1;
	}

	public void setQuestion1(int question1) {
		this.question1 = question1;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public int getQuestion2() {
		return question2;
	}

	public void setQuestion2(int question2) {
		this.question2 = question2;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public int getQuestion3() {
		return question3;
	}

	public void setQuestion3(int question3) {
		this.question3 = question3;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public int getQuestion4() {
		return question4;
	}

	public void setQuestion4(int question4) {
		this.question4 = question4;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public int getQuestion5() {
		return question5;
	}

	public void setQuestion5(int question5) {
		this.question5 = question5;
	}

	public String getAnswer5() {
		return answer5;
	}

	public void setAnswer5(String answer5) {
		this.answer5 = answer5;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getFeedbackComment() {
		return feedbackComment;
	}

	public void setFeedbackComment(String feedbackComment) {
		this.feedbackComment = feedbackComment;
	}

}
