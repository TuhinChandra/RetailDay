package com.tcs.novia.controller;

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

import com.tcs.novia.model.PhotographyContestImage;
import com.tcs.novia.repository.PhotographyContestImageRepository;
import com.tcs.novia.service.PhotoContestImageService;
import com.tcs.novia.service.PhotoContestResponseService;
import com.tcs.novia.util.ImageProcessor;

@Controller
public class PhotographyContestController {

	@Autowired
	PhotoContestImageService photoContestImageService;
	@Autowired
	PhotoContestResponseService photoContestResponseService;
	@Autowired
	ImageProcessor imageProcessor;
	@Autowired
	private PhotographyContestImageRepository photographyContestImageRepository;

	@RequestMapping(value = "/uploadPhoto/{owner}/{imageName}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PhotographyContestImage uploadPhoto(@PathVariable("owner") final String owner,
			@PathVariable("imageName") final String imageName, @RequestParam("photoFile") final MultipartFile photoFile)
			throws Exception {

		String photo = null;
		if (photoFile.isEmpty()) {
			throw new Exception("Oops!! Looks like photo is empty");
		}
		try {
			final byte[] bytes = photoFile.getBytes();
			photo = imageProcessor.resizeImage(bytes, 800, 600);
		} catch (final Exception e) {
			throw new Exception("Oops!! Looks like there is a technical issue. Try with different image.");
		}
		return photoContestImageService.uploadPhoto(new Random().nextInt(Integer.MAX_VALUE), owner, imageName, photo);
	}
	
	@RequestMapping(value = "/deletePhoto/{photoID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void getAllEvents(@PathVariable("photoID") final int photoID) {
		photographyContestImageRepository.deleteById(photoID);
	}

	@RequestMapping(value = "/getAllImages", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<PhotographyContestImage> getAllImages() {
		return photoContestImageService.getAllImages();
	}

	@RequestMapping(value = "/savePhotoContestResponse/{employeeID}/{photoId}/{vote}/{comment}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void savePhotoContestResponse(@PathVariable("employeeID") final String employeeID,
			@PathVariable("photoId") final int photoId, @PathVariable("vote") final String vote,
			@PathVariable("comment") final String comment) {
		photoContestResponseService.saveWithComment(Long.parseLong(employeeID), photoId, vote, comment);
	}
}
