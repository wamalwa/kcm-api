package com.univeit.kcm.controller;

import java.util.Date;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.univeit.kcm.config.JwtTokenUtil;
import com.univeit.kcm.model.ConfirmationToken;
import com.univeit.kcm.model.JwtRequest;
import com.univeit.kcm.model.JwtResponse;
import com.univeit.kcm.model.KcmUser;
import com.univeit.kcm.repo.ConfirmationTokenRepository;
import com.univeit.kcm.repo.UserRepository;
import com.univeit.kcm.service.JwtUserDetailsService;
import com.univeit.kcm.service.MailService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	MailService mailService;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody KcmUser user) throws Exception {
		KcmUser usr = userRepository.findByUsername(user.getUsername());
		
		if(usr != null) {
			KcmUser userExists = new KcmUser();
			userExists.setId(-1);
			return ResponseEntity.ok(userExists);
		} else {
			return ResponseEntity.ok(userDetailsService.save(user));
		}
		
	}
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());
		
		final String token = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new JwtResponse(token));
		
	}
	
	@RequestMapping(value = "/auth/logout", method = RequestMethod.POST)
	public ResponseEntity<?> deleteAuthenticationToken(){
		return ResponseEntity.ok("");
	}
	
	@RequestMapping(value = "user/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> login(@PathParam("id") int user) {
		KcmUser usr = new KcmUser();
		usr = userRepository.getOne(user);
		return ResponseEntity.ok(usr);
	}
	
	@RequestMapping(value = "user/me", method = RequestMethod.GET)
	public ResponseEntity<?> loggedIn(Authentication authentication) {
		KcmUser usr = new KcmUser();
		usr = userRepository.findByUsername(authentication.getName());
		return ResponseEntity.ok(usr);
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(@RequestBody KcmUser kcmUser){
		KcmUser existingUser = userRepository.findByUsername(kcmUser.getUsername());
		System.out.println(kcmUser.getUsername());
		if(existingUser != null) {
			ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);
			confirmationToken.setTokenStatus(1);
			confirmationToken.setTokenType(2);
			
			confirmationTokenRepository.save(confirmationToken);
			
			String txt = "Hi " + existingUser.getFullName().split(" ")[0] + ", To complete the password reset process, please click here: "
            + "https://www.catholicmusic.or.ke/reset-password/"+confirmationToken.getConfirmationToken();
			
			try {
				mailService.sendEmail(existingUser.getUsername(), "Password Recovery", txt);
			} catch (MailException mailException) {
				System.out.println(mailException);
			}
			
			return ResponseEntity.ok("Email sent successfully");
		}
		
		else {
			System.out.println("mail not sent");
			return ResponseEntity.ok("User Not Found");
		}
	}
	
	@RequestMapping(value = "/validate-token", method = RequestMethod.POST)
	public ResponseEntity<?> validateToken(@RequestBody ConfirmationToken confirmationToken){
		System.out.println(confirmationToken.getConfirmationToken());
		ConfirmationToken confirmedToken = new ConfirmationToken();
		
		confirmedToken = confirmationTokenRepository.findByConfirmationToken(confirmationToken.getConfirmationToken());
		
		Date currentTime = new Date();
		if (userDetailsService.timeDiffHours(currentTime, confirmedToken.getDateCreated()) <= 24 && confirmedToken.getTokenStatus() == 1) {
			return ResponseEntity.ok(confirmedToken);
		}
		else {
			ConfirmationToken invalidToken = new ConfirmationToken();
			return ResponseEntity.ok(invalidToken);
		}
	}
	
	@RequestMapping(value = "/confirm-reset", method = RequestMethod.POST)
	public ResponseEntity<?> confirmReset(@RequestBody ConfirmationToken confirmationToken){
		ConfirmationToken tkn = confirmationTokenRepository.findByConfirmationToken(confirmationToken.getConfirmationToken());
		
		Date currentTime = new Date();
		
		if(tkn != null && userDetailsService.timeDiffHours(currentTime, tkn.getDateCreated()) <= 24.2 && tkn.getTokenStatus() == 1 && tkn.getTokenType() == 2 ) {
			KcmUser usr = userRepository.findByUsername(confirmationToken.getKcmUser().getUsername());
			if(usr != null && usr.getId() == tkn.getKcmUser().getId()) {
				usr = userDetailsService.update(usr, confirmationToken.getKcmUser().getPassword());
				
				return ResponseEntity.ok("Success!");
			} else {
				return ResponseEntity.ok("Failed to reset password. The link could be broken, invalid or expired.");
			}
		} else {
			return ResponseEntity.ok("Failed to reset password. The link could be broken, invalid or expired.");
		}
	}
	
	
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
