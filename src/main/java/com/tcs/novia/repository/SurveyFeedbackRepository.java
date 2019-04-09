package com.tcs.novia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.SurveyFeedback;

@Repository
public interface SurveyFeedbackRepository extends JpaRepository<SurveyFeedback, Long> {

	List<SurveyFeedback> findByQuestion1(int questionId);
	List<SurveyFeedback> findByQuestion2(int questionId);
	List<SurveyFeedback> findByQuestion3(int questionId);
	List<SurveyFeedback> findByQuestion4(int questionId);
	List<SurveyFeedback> findByQuestion5(int questionId);
	
	List<SurveyFeedback> findByEmployeeID(long employeeID);
}
