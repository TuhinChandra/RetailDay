package com.tcs.KingfisherDay.model;

import java.util.List;

public class QuizResult {
	private OptionPercentage optionPercentage;
	private Employee winner;
	private Question question;
	private List<Employee> otherWinners;
	public QuizResult() {
	}

	public QuizResult(OptionPercentage optionPercentage, Employee winner, Question question) {
		super();
		this.optionPercentage = optionPercentage;
		this.winner = winner;
		this.question = question;
	}

	public QuizResult(OptionPercentage optionPercentage, Employee winner, Question question,
			List<Employee> otherWinners) {
		super();
		this.optionPercentage = optionPercentage;
		this.winner = winner;
		this.question = question;
		this.otherWinners = otherWinners;
	}

	public QuizResult(OptionPercentage optionPercentage, Question question) {
		super();
		this.optionPercentage = optionPercentage;
		this.question = question;
	}

	public OptionPercentage getOptionPercentage() {
		return optionPercentage;
	}

	public void setOptionPercentage(OptionPercentage optionPercentage) {
		this.optionPercentage = optionPercentage;
	}

	public Employee getWinner() {
		return winner;
	}

	public void setWinner(Employee winner) {
		this.winner = winner;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public List<Employee> getOtherWinners() {
		return otherWinners;
	}

	public void setOtherWinners(List<Employee> otherWinners) {
		this.otherWinners = otherWinners;
	}

}
