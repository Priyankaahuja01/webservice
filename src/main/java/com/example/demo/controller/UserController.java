package com.example.demo.controller;


import java.security.Principal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.Multitenantmanager;
import com.example.demo.model.Image;
import com.example.demo.model.User;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.UserResponse;
import com.example.demo.service.Service;
import com.example.demo.service.userService;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/v1")
public class UserController {
	@Autowired
	UserRepository userRepository;
//
	@Autowired
	userService userservice;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	Service service;
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	Multitenantmanager multitenantManager;
	
	@GetMapping("/user/self")
	public ResponseEntity<User> UserResponse (HttpServletRequest request,Authentication authentication , Principal p) {
		ResponseEntity<User> userResponse = null;
try{
		long startTime = System.currentTimeMillis();


		String upd = request.getHeader("authorization");
		if (upd == null || upd.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String pair = new String(Base64.decodeBase64(upd.substring(6)));
		String userName = pair.split(":")[0];
		String password = pair.split(":")[1];

		

		System.out.println("Setting for get request");
		multitenantManager.setCurrentTenant("all");

		System.out.println("In get /user/self");
		long startTime1 = System.currentTimeMillis();
		String name = p.getName();
		User users = userservice.loadUserByUsername(name);
	
			if (bCryptPasswordEncoder.matches(password, users.getPassword())) {

				
				return new ResponseEntity<>(users, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}


	}
	catch(Exception e)
	{
		System.out.println("Exception:"+e);
	}
return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	
	@PostMapping("/user")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		try {
			


			System.out.println("In post /user");
			long startTime = System.currentTimeMillis();
			if(user==null || user.getPassword() == null || user.getFirst_name() == null || 
					user.getUsername() == null || user.getLast_name() == null)
			{
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
			// check if already exists

			long startTime1 = System.currentTimeMillis();
			System.out.println("calling get user");	

			System.out.println("Setting for post request");
			multitenantManager.setCurrentTenant("all");
			Optional<User> u = userRepository.findByUsername(user.getUsername());


			System.out.println("checking if user is present");	
			if (u.isPresent()) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}


		         // encrypt password
					String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

					user.setPassword(encodedPassword);
					System.out.println("encoded: " + encodedPassword);

					long startTime2 = System.currentTimeMillis();
					User _user = userRepository
							.save(new User(user.getFirst_name(), user.getLast_name(), user.getPassword(), user.getUsername()));


					System.out.println("user saved in db, sending sns topic psoting call");	
					

					return new ResponseEntity<>(_user, HttpStatus.CREATED);
				} catch (Exception e) {
					System.out.println("exception: " +e);
					return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}
			}
	
	@PutMapping("/user/self")
	public ResponseEntity<String> updateTutorial(@RequestBody User user, HttpServletRequest request) {


		System.out.println("In put /user/self");
		long startTime = System.currentTimeMillis();

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		if (user.getFirst_name() == null || user.getFirst_name().isEmpty() || user.getFirst_name().isBlank()
				|| user.getLast_name() == null || user.getLast_name().isEmpty() || user.getLast_name().isBlank()
				|| user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().isBlank()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (user.getUsername() != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		String upd = request.getHeader("authorization");
		if (upd == null || upd.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String pair = new String(Base64.decodeBase64(upd.substring(6)));
		String userName = pair.split(":")[0];
		String password = pair.split(":")[1];

		
		System.out.println("Setting for put request");
		multitenantManager.setCurrentTenant("all");

		long startTime1 = System.currentTimeMillis();
		Optional<User> oldUser1 = userRepository.findByUsername(userName);

	
		if (oldUser1.isPresent()) {
			if (bCryptPasswordEncoder.matches(password, oldUser1.get().getPassword())) {// update
				
				
				
				User oldUser = oldUser1.get();
				oldUser.setFirst_name(user.getFirst_name());
				oldUser.setLast_name(user.getLast_name());
				oldUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				oldUser.setAccount_updated(OffsetDateTime.now(Clock.systemUTC()).toString());


				long startTime2 = System.currentTimeMillis();
				userRepository.save(oldUser);

				return new ResponseEntity<>("Update success", HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} else {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//post image
	 @PostMapping(value = "/user/self/pic")
	  public ResponseEntity<Image> createImage(@RequestParam(value="profilePic", required=true) MultipartFile profilePic, HttpServletRequest request)
			  throws Exception {
		 

		System.out.println("In post /user/self/pic");
		long startTime = System.currentTimeMillis();
		
		 String upd = request.getHeader("authorization");
			if (upd == null || upd.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			String pair = new String(Base64.decodeBase64(upd.substring(6)));
			String userName = pair.split(":")[0];
			String password = pair.split(":")[1];

			


			System.out.println("Setting for post request");
		
			Optional<User> tutorialData = userRepository.findByUsername(userName);// AndPassword(userName, encodedPass);
			Image img=null;
			if (tutorialData.isPresent()) {

				if (bCryptPasswordEncoder.matches(password, tutorialData.get().getPassword())) {


					
					User user = tutorialData.get();
					 
				
					Optional<Image> img1 = imageRepository.findByUserId(user.getId());
					if(img1.isPresent())
					{
						//delete
						long startTime2 = System.currentTimeMillis();
						String result = service.deleteFileFromS3Bucket(img1.get().getUrl(), user.getId());
						//statsd.increment("Calls - delete image by id");
				    	imageRepository.delete(img1.get());


					}
					
					
					
					String bucket_name =service.uploadFile( user.getId()+"/"+profilePic.getOriginalFilename(), profilePic);
					
					String url = bucket_name+"/"+ user.getId()+"/"+profilePic.getOriginalFilename(); 
					//create image
				
				    img = new Image(profilePic.getOriginalFilename(), user.getId(), url);
					long startTime2 = System.currentTimeMillis();
				    imageRepository.save(img);
		
				} else {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		 
		 
		
		
	    return new ResponseEntity<>(img, HttpStatus.CREATED);
	  }
	
	 
	 
	 
		//get image
	 @GetMapping(value = "/user/self/pic")
	  public ResponseEntity<Image> getImage(HttpServletRequest request)
			  throws Exception {
				System.out.println("In get /user/self/pic");

		long startTime = System.currentTimeMillis();

		 String upd = request.getHeader("authorization");
			if (upd == null || upd.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			String pair = new String(Base64.decodeBase64(upd.substring(6)));
			String userName = pair.split(":")[0];
			String password = pair.split(":")[1];

			System.out.println("Setting for get request");

			Optional<User> tutorialData = userRepository.findByUsername(userName);
			Optional<Image> img=null;
			if (tutorialData.isPresent()) {

				if (bCryptPasswordEncoder.matches(password, tutorialData.get().getPassword())) {


					
					User user = tutorialData.get();
										
					long startTime2 = System.currentTimeMillis();
				    img = imageRepository.findByUserId(user.getId());
				    if (img.isPresent()) {
				  
				    	return new ResponseEntity<>(img.get(), HttpStatus.OK);
						  }
				    else {
						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		 
	  }
	 
	 
	 
	 
		//delete image
	 @DeleteMapping(value = "/user/self/pic")
	  public ResponseEntity<String> deleteImage(HttpServletRequest request)
			  throws Exception {
				System.out.println("In delete /user/self/pic");
		long startTime = System.currentTimeMillis();
	
		 String upd = request.getHeader("authorization");
			if (upd == null || upd.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			String pair = new String(Base64.decodeBase64(upd.substring(6)));
			String userName = pair.split(":")[0];
			String password = pair.split(":")[1];

	


			System.out.println("Setting for delete request");
			
			Optional<User> tutorialData = userRepository.findByUsername(userName);// AndPassword(userName, encodedPass);
			Optional<Image> img=null;
			if (tutorialData.isPresent()) {

				if (bCryptPasswordEncoder.matches(password, tutorialData.get().getPassword())) {

					
					
					
					User user = tutorialData.get();
										
				
				    img = imageRepository.findByUserId(user.getId());
				    
				    if (img.isPresent()) {
				    	//so delete
				    	
				    	String result = service.deleteFileFromS3Bucket(img.get().getUrl(),user.getId());
						long startTime2 = System.currentTimeMillis();
				    	imageRepository.delete(img.get());

				    	
				    	return new ResponseEntity<>(result, HttpStatus.OK);
				    }
				    else {
						return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					}
				} else {
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		 
	  }
}
