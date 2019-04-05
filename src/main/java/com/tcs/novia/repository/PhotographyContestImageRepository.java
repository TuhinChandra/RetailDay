package com.tcs.novia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.PhotographyContestImage;

@Repository
public interface PhotographyContestImageRepository extends JpaRepository<PhotographyContestImage, Integer> {
	
	List<PhotographyContestImage> findByOwnerOrderByImageId(String owner);
}