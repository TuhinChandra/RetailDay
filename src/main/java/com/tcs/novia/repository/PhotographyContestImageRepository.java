package com.tcs.novia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.PhotographyContestImage;

@Repository
public interface PhotographyContestImageRepository extends JpaRepository<PhotographyContestImage, Integer> {

}