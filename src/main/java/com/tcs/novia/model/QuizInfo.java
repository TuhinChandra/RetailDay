package com.tcs.novia.model;

public class QuizInfo {

	private int noOfQuestions;
	private int currentQuestion;

	public QuizInfo(final int noOfQuestions, final int currentQuestion) {
		super();
		this.noOfQuestions = noOfQuestions;
		this.currentQuestion = currentQuestion;
	}

	public int getNoOfQuestions() {
		return noOfQuestions;
	}

	public void setNoOfQuestions(final int noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}

	public int getCurrentQuestion() {
		return currentQuestion;
	}

	public void setCurrentQuestion(final int currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

}
