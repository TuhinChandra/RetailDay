package com.tcs.KingfisherDay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tcs.KingfisherDay.model.Employee;
import com.tcs.KingfisherDay.service.UserService;
import com.tcs.KingfisherDay.util.ImageProcessor;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	ImageProcessor imageProcessor;

	@RequestMapping(value = "/registerEmployee/{name}/{emailID}/{password}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Employee registerEmployee(@PathVariable("name") String name, 
									 @PathVariable("emailID") String emailID, 
									 @RequestParam(value = "foodPreference", required = false) String foodPreference,
									 @PathVariable("password") String password, 
									 @RequestParam(value = "mobile", required = false) String mobile,
									 @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) throws Exception {
		
		Employee employee=userService.findByEmailID(emailID);
		
		if (null != employee) {
			String photo = employee.getPhoto();
			if (null!=photoFile && !photoFile.isEmpty()) {
				try {
					byte[] bytes = photoFile.getBytes();
					photo = imageProcessor.resizeImage(bytes);
				} catch (Exception e) {
					throw new Exception("Oops!! Looks like there is a technical issue. Try with different image.");
				}
			}
			return userService.register(name, emailID, foodPreference, password, mobile, photo);
		}
		else {
			throw new Exception("Oops!! Looks like your data is not available with us. Please contact administrator!!");
		}
	}

	@RequestMapping(value = "/getEmployee/{emailID}/{password}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Employee getEmployee(@PathVariable("emailID") String emailID, @PathVariable("password") String password) {
		return userService.getEmployee(emailID, password);
	}

	@RequestMapping(value = "/isExistsEmployee/{emailID}/{mobile}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Boolean isExistsEmployee(@PathVariable("emailID") String emailID, @PathVariable("mobile") String mobile) {
		return userService.isExistsEmployee(emailID, mobile);
	}

}
