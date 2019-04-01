package com.tcs.KingfisherDay.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.KingfisherDay.model.PhotoContestResponse;
import com.tcs.KingfisherDay.model.enums.Vote;
import com.tcs.KingfisherDay.repository.PhotoContestResponseRepository;
import com.tcs.KingfisherDay.repository.PhotographyContestImageRepository;

@Service
public class PhotoContestResponseService {

	@Autowired
	PhotographyContestImageRepository photographyContestImageRepository;

	@Autowired
	PhotoContestResponseRepository photoContestResponseRepository;

	public PhotoContestResponse save(String emailID, int imageId, String vote) {
		return photoContestResponseRepository.save(
				new PhotoContestResponse(imageId, emailID, Vote.valueOf(vote), new Timestamp(new Date().getTime())));
	}

	public PhotoContestResponse saveWithComment(String emailID, int imageId, String vote, String comment) {
		return photoContestResponseRepository.save(new PhotoContestResponse(imageId, emailID, Vote.valueOf(vote), comment,
				new Timestamp(new Date().getTime())));
	}


}
