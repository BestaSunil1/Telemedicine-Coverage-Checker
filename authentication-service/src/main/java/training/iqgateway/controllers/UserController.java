package training.iqgateway.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import training.iqgateway.dto.LoginRequest;
import training.iqgateway.entities.User;
import training.iqgateway.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/insert")
	public ResponseEntity<?> insertUser(@RequestBody User user) {
	    try {
	        User savedUser = userService.insertUser(user);
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	    } catch (Exception e) {
	        // You might want to catch specific exceptions (e.g. duplicate email) and handle accordingly
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body("Error inserting user: " + e.getMessage());
	    }
	}


	

}
