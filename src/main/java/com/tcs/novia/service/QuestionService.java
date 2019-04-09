package com.tcs.novia.service;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.novia.model.Question;
import com.tcs.novia.model.QuizInfo;
import com.tcs.novia.model.SurveyQuestion;
import com.tcs.novia.repository.QuestionRepository;
import com.tcs.novia.repository.SurveyQuestionRepository;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private SurveyQuestionRepository surveyQuestionRepository;
	@Autowired
	private ConfigurationService configurationService;

	public void setCurrentQuestion(final String questionID) {
		for (final Question question : questionRepository.findAll()) {
			if (question.getQuestionID().equals(questionID)) {
				question.setCurrent(true);
			} else {
				question.setCurrent(false);
			}
			questionRepository.save(question);
		}
	}

	public Question getCurrentQuestion() {
		final List<Question> activeQuestions = questionRepository.findByCurrent(true);
		if (null == activeQuestions || activeQuestions.isEmpty()) {
			return null;
		}
		return activeQuestions.get(0);
	}

	public Question getQuestion(final String questionID) {
		final List<Question> questions = questionRepository.findByQuestionID(questionID);
		if (null == questions || questions.isEmpty()) {
			return null;
		}
		return questions.get(0);
	}

	public void clearCurrentQuestion() {
		for (final Question question : questionRepository.findAll()) {
			question.setCurrent(false);
			questionRepository.save(question);
		}
	}

	public Question createQuizQuestion(final String questionID, final String questionText, final String optionA,
			final String optionB, final String optionC, final String optionD, final String optionCorrect) {
		final Question question = new Question(questionID, questionText, optionA, optionB, optionC, optionD,
				optionCorrect, false);
		return questionRepository.save(question);
	}
	public SurveyQuestion createSurveyQuestion(final String questionID, final String questionText, final String optionA,
			final String optionB, final String optionC, final String optionD) {
		final SurveyQuestion question = new SurveyQuestion(Integer.parseInt(questionID), questionText, optionA, optionB, optionC, optionD);
		return surveyQuestionRepository.save(question);
	}

	public QuizInfo getQuizInfo() {

		final int currentQuestion = null != getCurrentQuestion()
				? Integer.parseInt(getCurrentQuestion().getQuestionID())
				: 1;

		return new QuizInfo(configurationService.getNoOfQuizQuestions(), currentQuestion);
	}

	public void deleteQuestion(String questionID, Boolean isSurveyQuestion) {
		if(BooleanUtils.toBoolean(isSurveyQuestion)) {
			surveyQuestionRepository.deleteById(Integer.parseInt(questionID));
		}else {
			questionRepository.deleteById(questionID);
		}
	}

	public List<Question> getAllQuizQuestions() {
		return questionRepository.findAll();
	}
	public List<SurveyQuestion> getAllSurveyQuestion() {
		return surveyQuestionRepository.findAll();
	}

}
