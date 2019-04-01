package com.tcs.novia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.PhotoContestResponse;

@Repository
public interface PhotoContestResponseRepository extends JpaRepository<PhotoContestResponse, String> {


}
