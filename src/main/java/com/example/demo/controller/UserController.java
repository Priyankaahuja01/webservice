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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.Multitenantmanager;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.UserResponse;
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
	Multitenantmanager multitenantManager;
	
	@GetMapping("/user/self")
	public ResponseEntity<User> UserResponse (HttpServletRequest request,Authentication authentication , Principal p) {
		ResponseEntity<User> userResponse = null;
try{
		long startTime = System.currentTimeMillis();
        //statsd.increment("Calls - Get user/self - User");


		String upd = request.getHeader("authorization");
		if (upd == null || upd.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String pair = new String(Base64.decodeBase64(upd.substring(6)));
		String userName = pair.split(":")[0];
		String password = pair.split(":")[1];

		//System.out.println("username: " + userName);
		//System.out.println("password: " + password);

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
//	  UserDetailsRequest request = new UserDetailsRequest();
//	    request.setUsername(principal.getName());
//	    return userService.details(request);
//		System.out.println("principal");
//		String name = p.getName();
//User users = userservice.loadUserByUsername(name);
// userResponse = new UserResponse();
//userResponse.setId(users.getId());
//userResponse.setFirstName(users.getFirst_name());
//userResponse.setLastName(users.getLast_name());
//userResponse.setUsername(users.getUsername());
//userResponse.setAccount_created(users.getAccount_created());
//userResponse.setAccount_updated(users.getAccount_updated());

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
			//System.out.println("in post");
			//check values


			System.out.println("In post /user");
			long startTime = System.currentTimeMillis();
//            statsd.increment("Calls - Post user/ - Create new User");
			if(user==null || user.getPassword() == null || user.getFirst_name() == null || 
					user.getUsername() == null || user.getLast_name() == null)
			{
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
			// check if already exists

			long startTime1 = System.currentTimeMillis();
//			statsd.increment("Calls - find User by username");
			System.out.println("calling get user");	

			System.out.println("Setting for post request");
			multitenantManager.setCurrentTenant("all");
			Optional<User> u = userRepository.findByUsername(user.getUsername());

			//statsd.recordExecutionTime("DB Response Time - Get user", System.currentTimeMillis() - startTime1);

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

					//statsd.recordExecutionTime("DB Response Time - Save user in db", System.currentTimeMillis() - startTime2);

					System.out.println("user saved in db, sending sns topic psoting call");	
					//create entry in dynamodb to trigger lambda by sns
					//snsService.postToTopic("POST", _user.getUsername());

					//statsd.recordExecutionTime("Api Response Time - Post user/ - Create user",System.currentTimeMillis() - startTime);

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
		//statsd.increment("Calls - Put user/self - Update User");

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

		//System.out.println("username: " + userName);
		//System.out.println("password: " + password);
		
		System.out.println("Setting for put request");
		multitenantManager.setCurrentTenant("all");

		long startTime1 = System.currentTimeMillis();
		//statsd.increment("Calls - find User by username");
		Optional<User> oldUser1 = userRepository.findByUsername(userName);

		//statsd.recordExecutionTime("DB Response Time - Get user", System.currentTimeMillis() - startTime1);

		// validate password
		if (oldUser1.isPresent()) {
			if (bCryptPasswordEncoder.matches(password, oldUser1.get().getPassword())) {// update
				
				
				//check if verified user
//				if(!oldUser1.get().isVerified()) {
//					System.out.println("User is not yet verified");
//					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//				}
				
				
				User oldUser = oldUser1.get();
				oldUser.setFirst_name(user.getFirst_name());
				oldUser.setLast_name(user.getLast_name());
				oldUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				oldUser.setAccount_updated(OffsetDateTime.now(Clock.systemUTC()).toString());


				long startTime2 = System.currentTimeMillis();
				userRepository.save(oldUser);

				//statsd.recordExecutionTime("DB Response Time - Update user in db", System.currentTimeMillis() - startTime2);
				//statsd.recordExecutionTime("Api Response Time - Put user/self - Update user",System.currentTimeMillis() - startTime);
				return new ResponseEntity<>("Update success", HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} else {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
