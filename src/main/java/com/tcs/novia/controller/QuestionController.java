package com.tcs.novia.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tcs.novia.exception.CustomException;
import com.tcs.novia.model.Employee;
import com.tcs.novia.model.OptionPercentage;
import com.tcs.novia.model.Question;
import com.tcs.novia.model.QuizInfo;
import com.tcs.novia.model.QuizResponse;
import com.tcs.novia.model.QuizResult;
import com.tcs.novia.service.EmployeeService;
import com.tcs.novia.service.QuestionResponseService;
import com.tcs.novia.service.QuestionService;

@RestController
@SessionAttributes("name")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionResponseService quizResponseService;
	@Autowired
	private EmployeeService userService;
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@RequestMapping(value = "/addQuizQuestion/{questionID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Question addQuizQuestion(@PathVariable("questionID") final String questionID,
			@RequestParam(value = "questionText", required = true) final String questionText,
			@RequestParam(value = "optionA", required = true) final String optionA,
			@RequestParam(value = "optionB", required = true) final String optionB,
			@RequestParam(value = "optionC", required = false) final String optionC,
			@RequestParam(value = "optionD", required = false) final String optionD,
			@RequestParam(value = "optionCorrect", required = true) final String optionCorrect) {
		return questionService.createQuizQuestion(questionID, questionText, optionA, optionB, optionC, optionD,
				optionCorrect);
	}
	
	@RequestMapping(value = "/deleteQuestion/{questionID}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void deleteQuestion(@PathVariable("questionID") final String questionID) throws Exception {
		questionService.deleteQuestion(questionID, false);
	}

	@RequestMapping(value = "/setCurrentQuestion/{questionID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void setCurrentQuestion(@PathVariable("questionID") final String questionID) {
		questionService.setCurrentQuestion(questionID);
		messagingTemplate.convertAndSend("/topic/broadcastCurrentQuestion", questionService.getCurrentQuestion());
	}

	@RequestMapping(value = "/clearCurrentQuestion", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void clearCurrentQuestion() {
		questionService.clearCurrentQuestion();
		messagingTemplate.convertAndSend("/topic/broadcastCurrentQuestion",
				"{\"questionUnavailbleText\":\"Quiz is not available now!!\"}");
	}

	@RequestMapping(value = "/saveResponse/{questionID}/{employeeID}/{option}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public QuizResponse saveQuizResponse(@PathVariable("questionID") final String questionID,
			@PathVariable("employeeID") final String employeeID, @PathVariable("option") final String option) throws CustomException {
		return quizResponseService.saveQuizResponse(questionID, Long.parseLong(employeeID), option);
	}

	@RequestMapping(value = "/getResult/{questionID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public QuizResult getResult(@PathVariable("questionID") final String questionID) {
		final Question question = questionService.getQuestion(questionID);
		final List<QuizResponse> winnerResponse = quizResponseService.getAllWinners(questionID, question);
		final OptionPercentage optionPercentage = quizResponseService.getPercentages(question);
		QuizResult quizResult = new QuizResult(optionPercentage, question);
		if (winnerResponse != null && !winnerResponse.isEmpty()) {
			final List<Employee> allWinners = new ArrayList<>(winnerResponse.size());
			for (final QuizResponse eachWinnerResponse : winnerResponse) {
				final Employee winner = userService.findByEmployeeID(eachWinnerResponse.getEmployeeID());
				if (null != winner) {
					allWinners.add(winner);
				}
			}
			if (!allWinners.isEmpty()) {
				quizResult = new QuizResult(optionPercentage, allWinners.get(0), question, allWinners);
			}
		}
		return quizResult;
	}

	@RequestMapping(value = "/getCurrentQuestionAdmin", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Question getCurrentQuestion() {
		return questionService.getCurrentQuestion();
	}
	
	@RequestMapping(value = "/getAllQuizQuestions", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Question> getAllQuizQuestions() {
		return questionService.getAllQuizQuestions();
	}

	@MessageMapping("/getCurrentQuestion")
	public void sendMessage(final Principal principal, @SuppressWarnings("rawtypes") final Map message) {
		if (questionService.getCurrentQuestion() != null) {
			messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/getCurrentQuestion",
					questionService.getCurrentQuestion());
		} else {
			messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/getCurrentQuestion",
					"{\"questionUnavailbleText\":\"Quiz is not available now!!\"}");
		}
	}

	@RequestMapping(value = "/getQuizInfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public QuizInfo getQuizInfo() {
		return questionService.getQuizInfo();
	}
	
	@PostMapping(value = "/clearQuizResponse", produces = "application/json")
	@ResponseBody
	public void clearQuizResponse(@RequestParam(value = "questionID", required = false) final String questionID,
			@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		quizResponseService.clearQuizResponse(questionID, employeeID);
	}
	
	@GetMapping(value = "/getQuizResponse", produces = "application/json")
	@ResponseBody
	public List<QuizResponse> getQuizResponse(@RequestParam(value = "questionID", required = false) final String questionID,
			@RequestParam(value = "employeeID", required = false) final Long employeeID) {
		return quizResponseService.getQuizResponse(questionID, employeeID);
	}

}
