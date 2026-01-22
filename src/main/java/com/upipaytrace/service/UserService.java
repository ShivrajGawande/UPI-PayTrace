package com.upipaytrace.service;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.upipaytrace.dto.JwtAuthenticationResponse;
import com.upipaytrace.dto.LoginDto;
import com.upipaytrace.dto.RegisterRequestDto;
import com.upipaytrace.entity.User;
import com.upipaytrace.repository.UserRepository;
import com.upipaytrace.security.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

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

    public JwtAuthenticationResponse loginUser(LoginDto loginDto) {

    	 	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                         			loginDto.getEmail(),loginDto.getPassword()));

         String token = jwtUtils.generateToken(loginDto.getEmail());

         return new JwtAuthenticationResponse(token);
     }
    
    
}
