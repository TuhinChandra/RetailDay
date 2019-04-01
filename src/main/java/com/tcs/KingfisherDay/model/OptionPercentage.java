package com.tcs.KingfisherDay.model;

public class OptionPercentage {
	private double optionA;
	private double optionB;
	private double optionC;
	private double optionD;

	public OptionPercentage() {

	}

	public OptionPercentage(double optionA, double optionB, double optionC, double optionD) {
		super();
		this.optionA = optionA;
		this.optionB = optionB;
		this.optionC = optionC;
		this.optionD = optionD;
	}

	public double getOptionA() {
		return optionA;
	}

	public void setOptionA(double optionA) {
		this.optionA = optionA;
	}

	public double getOptionB() {
		return optionB;
	}

	public void setOptionB(double optionB) {
		this.optionB = optionB;
	}

	public double getOptionC() {
		return optionC;
	}

	public void setOptionC(double optionC) {
		this.optionC = optionC;
	}

	public double getOptionD() {
		return optionD;
	}

	public void setOptionD(double optionD) {
		this.optionD = optionD;
	}

	@Override
	public String toString() {
		return "OptionPercentage [optionA=" + optionA + ", optionB=" + optionB + ", optionC=" + optionC + ", optionD="
				+ optionD + "]";
	}

}
