package com.tcs.KingfisherDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.KingfisherDay.model.PhotographyContestImage;

@Repository
public interface PhotographyContestImageRepository extends JpaRepository<PhotographyContestImage, String> {

}