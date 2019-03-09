package com.tcs.novia.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tcs.novia.model.OptionPercentage;
import com.tcs.novia.model.Question;
import com.tcs.novia.model.QuizResponse;
import com.tcs.novia.repository.QuizResponseRepository;

@Service
public class QuizResponseService {

	@Autowired
	QuizResponseRepository quizResponseRepository;

	@Value("${quiz.show.number.of.winners}")
	private int numberOfWinnersToShow;

	public QuizResponse saveResponse(final String questionID, final long employeeID, final String option) {
		return quizResponseRepository
				.save(new QuizResponse(questionID, employeeID, option, new Timestamp(new Date().getTime())));
	}

	public QuizResponse getWinner(final String questionID, final Question question) {
		final List<QuizResponse> winners = quizResponseRepository.findTopByQuestionIDAndOptionOrderByTimeStamp(
				questionID, question.getOptionCorrect(), PageRequest.of(0, 1));
		return null != winners && !winners.isEmpty() ? winners.get(0) : null;
	}

	public List<QuizResponse> getAllWinners(final String questionID, final Question question) {
		final List<QuizResponse> winners = quizResponseRepository.findTopByQuestionIDAndOptionOrderByTimeStamp(
				questionID, question.getOptionCorrect(), PageRequest.of(0, numberOfWinnersToShow));
		return winners;
	}

	public OptionPercentage getPercentages(final Question question) {
		long total = quizResponseRepository.countByQuestionID(question.getQuestionID());
		if (total == 0) {
			return new OptionPercentage(0, 0, 0, 0);
		}
		final long totalA = quizResponseRepository.countByOptionAndQuestionID(question.getOptionA(),
				question.getQuestionID());
		final long totalB = quizResponseRepository.countByOptionAndQuestionID(question.getOptionB(),
				question.getQuestionID());
		final long totalC = quizResponseRepository.countByOptionAndQuestionID(question.getOptionC(),
				question.getQuestionID());
		final long totalD = quizResponseRepository.countByOptionAndQuestionID(question.getOptionD(),
				question.getQuestionID());
		total = totalA + totalB + totalC + totalD;
		System.out.println("For Question:" + question.toString());
		System.out.println("Option A answered by=" + totalA);
		System.out.println("Option B answered by=" + totalB);
		System.out.println("Option C answered by=" + totalC);
		System.out.println("Option D answered by=" + totalD);
		System.out.println("Total=" + total);
		final OptionPercentage optionPercentage = new OptionPercentage(getRoundedValue(totalA, total),
				getRoundedValue(totalB, total), getRoundedValue(totalC, total), getRoundedValue(totalD, total));
		System.out.println(optionPercentage);
		return optionPercentage;
	}

	public static double getRoundedValue(final long option, final long totalAnser) {

		double doubleValue = (double) option / (double) totalAnser;
		doubleValue *= 100;
		final DecimalFormat df = new DecimalFormat("###.##");
		final double roundedValue = Double.parseDouble(df.format(doubleValue));
		return roundedValue;

	}

}
