package com.tcs.novia.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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
		
		LOGGER.trace("For Question:{}", question.toString());
		LOGGER.trace("Option A answered by:{}", totalA);
		LOGGER.trace("Option B answered by:{}", totalB);
		LOGGER.trace("Option C answered by:{}", totalC);
		LOGGER.trace("Option D answered by:{}", totalD);
		LOGGER.trace("Total:{}", total);
		
		final OptionPercentage optionPercentage = new OptionPercentage(getRoundedValue(totalA, total),
				getRoundedValue(totalB, total), getRoundedValue(totalC, total), getRoundedValue(totalD, total));

		LOGGER.trace("optionPercentage:{}", optionPercentage);
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
