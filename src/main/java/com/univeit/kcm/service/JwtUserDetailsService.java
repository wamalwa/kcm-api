package com.univeit.kcm.service;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.univeit.kcm.model.KcmUser;
import com.univeit.kcm.repo.UserRepository;

import net.bytebuddy.utility.RandomString;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		KcmUser user = userRepository.findByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException(username + " not found.");
		}
		
		return new User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}
	
	public KcmUser save(KcmUser usr) {
		String activation = RandomString.make(7);
		
		KcmUser newUser = new KcmUser();
		
		newUser.setFullName(usr.getFullName());
		newUser.setUsername(usr.getUsername());
		newUser.setPassword(bcryptEncoder.encode(usr.getPassword()));
		newUser.setActivationCode(bcryptEncoder.encode(activation));
		newUser.setRole(2);
		
		return userRepository.save(newUser);
	}
	
	public KcmUser update(KcmUser usr, String newPassword) {
		usr.setPassword(bcryptEncoder.encode(newPassword));
		return userRepository.save(usr);
	}
	
	public float timeDiffHours(Date a, Date b) {
		return (float) ((a.getTime() - b.getTime())/3600000.00);
	}
}
