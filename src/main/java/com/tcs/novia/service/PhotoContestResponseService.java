package com.tcs.novia.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.novia.model.PhotoContestResponse;
import com.tcs.novia.model.enums.Vote;
import com.tcs.novia.repository.PhotoContestResponseRepository;
import com.tcs.novia.repository.PhotographyContestImageRepository;

@Service
public class PhotoContestResponseService {

	@Autowired
	PhotographyContestImageRepository photographyContestImageRepository;

	@Autowired
	PhotoContestResponseRepository photoContestResponseRepository;

	public PhotoContestResponse save(final long employeeID, final int imageId, final String vote) {
		return photoContestResponseRepository.save(
				new PhotoContestResponse(imageId, employeeID, Vote.valueOf(vote), new Timestamp(new Date().getTime())));
	}

	public PhotoContestResponse saveWithComment(final long employeeID, final int imageId, final String vote,
			final String comment) {
		return photoContestResponseRepository.save(new PhotoContestResponse(imageId, employeeID, Vote.valueOf(vote),
				comment, new Timestamp(new Date().getTime())));
	}

}
