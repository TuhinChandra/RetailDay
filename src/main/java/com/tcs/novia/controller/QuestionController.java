package com.tcs.novia.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tcs.novia.model.Employee;
import com.tcs.novia.model.OptionPercentage;
import com.tcs.novia.model.Question;
import com.tcs.novia.model.QuizResponse;
import com.tcs.novia.model.QuizResult;
import com.tcs.novia.service.EmployeeService;
import com.tcs.novia.service.QuestionService;
import com.tcs.novia.service.QuizResponseService;

@RestController
@SessionAttributes("name")
public class QuestionController {

	@Autowired
	QuestionService questionService;

	@Autowired
	QuizResponseService quizResponseService;

	@Autowired
	EmployeeService userService;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

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
	public QuizResponse saveResponse(@PathVariable("questionID") final String questionID,
			@PathVariable("employeeID") final String employeeID, @PathVariable("option") final String option) {
		return quizResponseService.saveResponse(questionID, Long.parseLong(employeeID), option);
	}

	@RequestMapping(value = "/getResult/{questionID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public QuizResult getResult(@PathVariable("questionID") final String questionID) {
		final Question question = questionService.getQuestion(questionID);
		final List<QuizResponse> winnerResponse = quizResponseService.getAllWinners(questionID, question);
		final OptionPercentage optionPercentage = quizResponseService.getPercentages(question);
		if (winnerResponse != null && !winnerResponse.isEmpty()) {
			final List<Employee> allWinners = new ArrayList<>(winnerResponse.size());
			for (final QuizResponse eachWinnerResponse : winnerResponse) {
				final Employee winner = userService.findByEmployeeID(eachWinnerResponse.getEmployeeID());
				allWinners.add(winner);
			}
			return new QuizResult(optionPercentage, allWinners.get(0), question, allWinners);
		}
		return new QuizResult(optionPercentage, question);
	}

	@RequestMapping(value = "/getCurrentQuestionAdmin", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Question getResult() {
		return questionService.getCurrentQuestion();
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

}
