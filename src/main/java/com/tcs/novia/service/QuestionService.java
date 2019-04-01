package com.tcs.novia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.novia.model.Question;
import com.tcs.novia.repository.QuestionRepository;

@Service
public class QuestionService {

	@Autowired
	QuestionRepository questionRepository;

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

	public Question createQuestion(final String questionID, final String questionText, final String optionA,
			final String optionB, final String optionC, final String optionD, final String optionCorrect) {
		final Question question = new Question(questionID, questionText, optionA, optionB, optionC, optionD,
				optionCorrect, false);
		return questionRepository.save(question);
	}

}
