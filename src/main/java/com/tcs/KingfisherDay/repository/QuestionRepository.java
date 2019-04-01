package com.tcs.KingfisherDay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.KingfisherDay.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
	List<Question> findByCurrent(boolean current);

	List<Question> findByQuestionID(String questionID);
}
