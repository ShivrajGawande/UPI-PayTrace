package com.upipaytrace.service;


import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.upipaytrace.dto.LoginDto;
import com.upipaytrace.dto.RegisterRequestDto;
import com.upipaytrace.entity.User;
import com.upipaytrace.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(RegisterRequestDto requestDto) {

    		if (userRepository.existsByEmail(requestDto.getEmail())) 
        {
            throw new RuntimeException("Email already exists");
        }
    		
    		 User userEntity = new User();
    	     BeanUtils.copyProperties(requestDto, userEntity);

    	     userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
    	     
         return "User Created successfully with email :: "
         +userRepository.save(userEntity).getEmail();
    }

    public String loginUser(LoginDto loginDto) {

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return "Login success";
        } 
        else
        {
            throw new RuntimeException("Invalid password");
        }
    }
    
    
}
