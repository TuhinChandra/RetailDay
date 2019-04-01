package com.tcs.KingfisherDay.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tcs.KingfisherDay.model.PhotographyContestImage;
import com.tcs.KingfisherDay.service.PhotoContestImageService;
import com.tcs.KingfisherDay.service.PhotoContestResponseService;
import com.tcs.KingfisherDay.util.ImageProcessor;

@Controller
public class PhotographyContestController {

	@Autowired
	PhotoContestImageService photoContestImageService;
	@Autowired
	PhotoContestResponseService photoContestResponseService;
	@Autowired
	ImageProcessor imageProcessor;

	@RequestMapping(value = "/uploadPhoto/{owner}/{imageName}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PhotographyContestImage uploadPhoto(@PathVariable("owner") String owner,
			@PathVariable("imageName") String imageName, @RequestParam("photoFile") MultipartFile photoFile)
			throws Exception {

		String photo = null;
		if (photoFile.isEmpty()) {
			throw new Exception("Oops!! Looks like photo is empty");
		}
		try {
			byte[] bytes = photoFile.getBytes();
			photo = imageProcessor.resizeImage(bytes, 800, 600);
		} catch (Exception e) {
			throw new Exception("Oops!! Looks like there is a technical issue. Try with different image.");
		}
		return photoContestImageService.uploadPhoto(new Random().nextInt(Integer.MAX_VALUE), owner, imageName, photo);
	}

	@RequestMapping(value = "/getAllImages", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<PhotographyContestImage> getAllImages() {
		return photoContestImageService.getAllImages();
	}

	@RequestMapping(value = "/savePhotoContestResponse/{emailID}/{photoId}/{vote}/{comment}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void savePhotoContestResponse(@PathVariable("emailID") String emailID, @PathVariable("photoId") int photoId,
			@PathVariable("vote") String vote, @PathVariable("comment") String comment) {
		photoContestResponseService.saveWithComment(emailID, photoId, vote, comment);
	}
}
