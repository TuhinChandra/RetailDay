package com.tcs.novia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.novia.exception.CustomException;
import com.tcs.novia.model.SurveyFeedback;
import com.tcs.novia.model.SurveyQuestion;
import com.tcs.novia.repository.SurveyQuestionRepository;
import com.tcs.novia.service.QuestionResponseService;
import com.tcs.novia.service.QuestionService;

@RestController
public class SurveyController {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionResponseService quizResponseService;
	@Autowired
	private SurveyQuestionRepository surveyQuestionRepository;

	@RequestMapping(value = "/addSurveyQuestion/{questionID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public SurveyQuestion addSurveyQuestion(@PathVariable("questionID") final String questionID,
			@RequestParam(value = "questionText", required = true) final String questionText,
			@RequestParam(value = "optionA", required = true) final String optionA,
			@RequestParam(value = "optionB", required = true) final String optionB,
			@RequestParam(value = "optionC", required = false) final String optionC,
			@RequestParam(value = "optionD", required = false) final String optionD) {
		return questionService.createSurveyQuestion(questionID, questionText, optionA, optionB, optionC, optionD);
	}
	
	@RequestMapping(value = "/addSurveyQuestionFromJSON", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public List<SurveyQuestion> addSurveyQuestionFromJSON(@RequestBody final List<SurveyQuestion> surveyQuestions) {
		return surveyQuestionRepository.saveAll(surveyQuestions);
	}

	@RequestMapping(value = "/deleteSurveyQuestion/{questionID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void deleteSurveyQuestion(@PathVariable("questionID") final String questionID) {
		questionService.deleteQuestion(questionID, true);
	}

	@RequestMapping(value = "/getAllSurveyQuestion", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<SurveyQuestion> getAllSurveyQuestion() {
		return questionService.getAllSurveyQuestion();
	}

	@PostMapping(value = "/saveSurveyFeedback", produces = "application/json")
	@ResponseBody
	public SurveyFeedback saveSurveyFeedback(@RequestParam(value = "employeeID", required = true) final Long employeeID,
			@RequestParam(value = "question1", required = true) final Integer question1,
			@RequestParam(value = "question2", required = true) final Integer question2,
			@RequestParam(value = "question3", required = true) final Integer question3,
			@RequestParam(value = "question4", required = true) final Integer question4,
			@RequestParam(value = "question5", required = true) final Integer question5,
			@RequestParam(value = "answer1", required = true) final String answer1,
			@RequestParam(value = "answer2", required = true) final String answer2,
			@RequestParam(value = "answer3", required = true) final String answer3,
			@RequestParam(value = "answer4", required = true) final String answer4,
			@RequestParam(value = "answer5", required = true) final String answer5,
			@RequestParam(value = "feedbackComment", required = false) final String feedbackComment)
			throws CustomException {
		return quizResponseService.saveSurveyFeedback(employeeID, question1, answer1, question2, answer2, question3,
				answer3, question4, answer4, question5, answer5, feedbackComment);
	}

	@PostMapping(value = "/clearSurveyFeedback", produces = "application/json")
	@ResponseBody
	public void clearSurveyFeedback(@RequestParam(value = "questionID", required = false) final Integer questionID,
			@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		quizResponseService.clearSurveyFeedback(questionID, employeeID);
	}

	@GetMapping(value = "/getSurveyFeedback", produces = "application/json")
	@ResponseBody
	public List<SurveyFeedback> getSurveyFeedback(
			@RequestParam(value = "questionID", required = false) final Integer questionID,
			@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return quizResponseService.getSurveyFeedback(questionID, employeeID);
	}
}
