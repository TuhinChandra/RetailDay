package com.tcs.novia.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tcs.novia.exception.CustomException;
import com.tcs.novia.model.Employee;
import com.tcs.novia.model.OptionPercentage;
import com.tcs.novia.model.Question;
import com.tcs.novia.model.QuizResponse;
import com.tcs.novia.model.SurveyFeedback;
import com.tcs.novia.repository.QuizResponseRepository;
import com.tcs.novia.repository.SurveyFeedbackRepository;

@Service
public class QuestionResponseService {

	@Autowired
	private QuizResponseRepository quizResponseRepository;
	
	@Autowired
	private SurveyFeedbackRepository surveyFeedbackRepository;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private ConfigurationService configurationService;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public QuizResponse saveQuizResponse(final String questionID, final long employeeID, final String option) throws CustomException {

		QuizResponse existingQuizResponse = quizResponseRepository.findByQuestionIDAndEmployeeID(questionID,
				employeeID);

		if (null == existingQuizResponse) {
			Employee emp=employeeService.findByEmployeeID(employeeID);
			if(null==emp) {
				throw new CustomException("No employee found against employeeID ::" + employeeID);
			}
			existingQuizResponse = quizResponseRepository
					.save(new QuizResponse(questionID, employeeID, option, LocalDateTime.now()));
		} else {
			existingQuizResponse.setAlreadyAnswered(true);
		}

		return existingQuizResponse;
	}

	public QuizResponse getQuizWinner(final String questionID, final Question question) {
		final List<QuizResponse> winners = quizResponseRepository.findTopByQuestionIDAndOptionOrderByTimeStamp(
				questionID, question.getOptionCorrect(), PageRequest.of(0, 1));
		return null != winners && !winners.isEmpty() ? winners.get(0) : null;
	}

	public List<QuizResponse> getAllWinners(final String questionID, final Question question) {
		final List<QuizResponse> winners = quizResponseRepository.findTopByQuestionIDAndOptionOrderByTimeStamp(
				questionID, question.getOptionCorrect(),
				PageRequest.of(0, configurationService.getNoOfDisplayableQuizWinners()));
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
	
	public void clearQuizResponse(final String questionID, final Long employeeID) {
		quizResponseRepository.deleteAll(getQuizResponse(questionID, employeeID));
	}
	
	public List<QuizResponse> getQuizResponse(final String questionID, final Long employeeID) {
		List<QuizResponse> quizResponses = null;
		if (StringUtils.isNotBlank(questionID) && (null!=employeeID && employeeID > 0)) {
			QuizResponse quizResponse = quizResponseRepository.findByQuestionIDAndEmployeeID(questionID, employeeID);
			if (null != quizResponse) {
				quizResponses = new ArrayList<QuizResponse>();
				quizResponses.add(quizResponse);
			}
		} else if (StringUtils.isNotBlank(questionID)) {
			quizResponses = quizResponseRepository.findByQuestionID(questionID);
		} else if (null!=employeeID && employeeID > 0) {
			quizResponses = quizResponseRepository.findByEmployeeID(employeeID);
		} else {
			quizResponses = quizResponseRepository.findAll();
		}
		return quizResponses;
	}
	
	public SurveyFeedback saveSurveyFeedback(long employeeID, 
			int question1, String answer1, 
			int question2, String answer2, 
			int question3, String answer3, 
			int question4, String answer4, 
			int question5, String answer5, 
			String feedbackComment) throws CustomException {
		Employee emp=employeeService.findByEmployeeID(employeeID);
		if(null==emp) {
			throw new CustomException("No employee found against employeeID ::" + employeeID);
		}
		return surveyFeedbackRepository.save(new SurveyFeedback(employeeID, question1, answer1, question2, answer2, question3, answer3, question4, answer4, question5, answer5, LocalDateTime.now(), feedbackComment));
	}
	
	public void clearSurveyFeedback(final Integer questionID, final Long employeeID) {
		surveyFeedbackRepository.deleteAll(getSurveyFeedback(questionID, employeeID));
	}
	
	public List<SurveyFeedback> getSurveyFeedback(final Integer questionID, final Long employeeID) {
		List<SurveyFeedback> surveyFeedback = null;
		if (null != questionID && questionID > 0) {
			switch (questionID) {
			case 1:
				surveyFeedback = surveyFeedbackRepository.findByQuestion1(questionID);
				break;
			case 2:
				surveyFeedback = surveyFeedbackRepository.findByQuestion2(questionID);
				break;
			case 3:
				surveyFeedback = surveyFeedbackRepository.findByQuestion3(questionID);
				break;
			case 4:
				surveyFeedback = surveyFeedbackRepository.findByQuestion4(questionID);
				break;
			case 5:
				surveyFeedback = surveyFeedbackRepository.findByQuestion5(questionID);
				break;
			}
		} else if (null!=employeeID && employeeID > 0) {
			surveyFeedback = surveyFeedbackRepository.findByEmployeeID(employeeID);
		} else {
			surveyFeedback = surveyFeedbackRepository.findAll();
		}
		return surveyFeedback;
	}
}
