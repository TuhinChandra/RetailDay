package com.tcs.KingfisherDay.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tcs.KingfisherDay.model.OptionPercentage;
import com.tcs.KingfisherDay.model.Question;
import com.tcs.KingfisherDay.model.QuizResponse;
import com.tcs.KingfisherDay.repository.QuizResponseRepository;

@Service
public class QuizResponseService {

	@Autowired
	QuizResponseRepository quizResponseRepository;
	
	@Value("${quiz.show.number.of.winners}")
	private int numberOfWinnersToShow;

	public QuizResponse saveResponse(String questionID, String employeeEmail, String option) {
		return quizResponseRepository
				.save(new QuizResponse(questionID, employeeEmail, option, new Timestamp(new Date().getTime())));
	}

	public QuizResponse getWinner(String questionID, Question question) {
		List<QuizResponse> winners = quizResponseRepository.findTopByQuestionIDAndOptionOrderByTimeStamp(questionID,
				question.getOptionCorrect(), PageRequest.of(0, 1));
		return null != winners && !winners.isEmpty() ? winners.get(0) : null;
	}

	public List<QuizResponse> getAllWinners(String questionID, Question question) {
		List<QuizResponse> winners = quizResponseRepository.findTopByQuestionIDAndOptionOrderByTimeStamp(questionID,
				question.getOptionCorrect(), PageRequest.of(0, numberOfWinnersToShow));
		return  winners;
	}

	public OptionPercentage getPercentages(Question question) {
		long total = quizResponseRepository.countByQuestionID(question.getQuestionID());
		if (total == 0)
			return new OptionPercentage(0, 0, 0, 0);
		long totalA = quizResponseRepository.countByOptionAndQuestionID(question.getOptionA(), question.getQuestionID());
		long totalB = quizResponseRepository.countByOptionAndQuestionID(question.getOptionB(), question.getQuestionID());
		long totalC = quizResponseRepository.countByOptionAndQuestionID(question.getOptionC(), question.getQuestionID());
		long totalD = quizResponseRepository.countByOptionAndQuestionID(question.getOptionD(), question.getQuestionID());
		total = totalA + totalB + totalC + totalD;
		System.out.println("For Question:"+question.toString());
		System.out.println("Option A answered by="+totalA);
		System.out.println("Option B answered by="+totalB);
		System.out.println("Option C answered by="+totalC);
		System.out.println("Option D answered by="+totalD);
		System.out.println("Total="+total);
		OptionPercentage optionPercentage=new OptionPercentage(
				getRoundedValue( totalA , total), 
				getRoundedValue( totalB , total), 
				getRoundedValue( totalC , total),
				getRoundedValue( totalD , total)
				);
		System.out.println(optionPercentage);
		return optionPercentage;
	}
	
	public static double getRoundedValue(long option, long totalAnser) {

		double doubleValue = (double) option / (double) totalAnser;
		doubleValue*=100;
		DecimalFormat df = new DecimalFormat("###.##");
		double roundedValue = Double.parseDouble(df.format(doubleValue));
		return roundedValue;

	}

}
