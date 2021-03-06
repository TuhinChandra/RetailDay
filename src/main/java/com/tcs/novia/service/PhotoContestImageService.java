package com.tcs.novia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.novia.model.PhotographyContestImage;
import com.tcs.novia.repository.PhotographyContestImageRepository;

@Service
public class PhotoContestImageService {

	@Autowired
	PhotographyContestImageRepository photographyContestImageRepository;

	public List<PhotographyContestImage> getAllImages() {
		return photographyContestImageRepository.findAll();
	}

	public PhotographyContestImage uploadPhoto(int imageId, String owner, String imageName, String content) {
		return photographyContestImageRepository.save(new PhotographyContestImage(imageId, owner, imageName, content));
	}

}