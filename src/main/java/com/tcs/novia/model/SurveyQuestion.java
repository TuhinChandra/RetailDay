package com.tcs.novia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SURVEY_QUESTION")
public class SurveyQuestion {

	@Id
	@Column(name = "QUESTION_ID", nullable = false)
	private int questionID;
	@Column(name = "QUESTION_TEXT", nullable = false)
	private String questionText;
	@Column(name = "OPTION_A", nullable = false)
	private String optionA;
	@Column(name = "OPTION_B", nullable = false)
	private String optionB;
	@Column(name = "OPTION_C", nullable = true)
	private String optionC;
	@Column(name = "OPTION_D", nullable = true)
	private String optionD;

	public SurveyQuestion(int questionID, String questionText, String optionA, String optionB, String optionC,
			String optionD) {
		this.questionID = questionID;
		this.questionText = questionText;
		this.optionA = optionA;
		this.optionB = optionB;
		this.optionC = optionC;
		this.optionD = optionD;
	}

	public SurveyQuestion() {

	}

	public int getQuestionID() {
		return questionID;
	}

	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getOptionA() {
		return optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public String getOptionB() {
		return optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}

	public String getOptionC() {
		return optionC;
	}

	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}

	public String getOptionD() {
		return optionD;
	}

	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}
}