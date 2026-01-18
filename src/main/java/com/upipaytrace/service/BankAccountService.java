package com.upipaytrace.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.upipaytrace.dto.BankAccountRequestDto;
import com.upipaytrace.dto.BankAccountResponceDto;
import com.upipaytrace.entity.BankAccount;
import com.upipaytrace.entity.User;
import com.upipaytrace.repository.BankAccountRepository;
import com.upipaytrace.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    public String addBankAccount(BankAccountRequestDto bankAccountDto, Principal principal) {
    	
    		User user = userRepository.findByEmail(principal.getName())
    				.orElseThrow(()-> new RuntimeException("User not found"));
        boolean exists = bankAccountRepository
                .existsByUserAndMaskedAccountNumber(user, bankAccountDto.getMaskedAccountNumber());

        if (exists) {
            throw new RuntimeException("Bank account already added");
        }
        
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(bankAccountDto, bankAccount);
        bankAccount.setUser(user);
        bankAccountRepository.save(bankAccount);
        return "Bank Account added Successfully..";
    }


    public List<BankAccountResponceDto> getUserBankAccounts(Principal principal) {
    	
    		User user = userRepository.findByEmail(principal.getName())
				.orElseThrow(()-> new RuntimeException("User not found"));
    		List<BankAccount> listBankAcc =  bankAccountRepository.findByUser(user);
    		List<BankAccountResponceDto> listBankAccDto = new ArrayList<>();
    		
    		for(BankAccount bankAcc : listBankAcc) {
    			BankAccountResponceDto bankAccDto = new BankAccountResponceDto();
    			BeanUtils.copyProperties(bankAcc, bankAccDto);
    			listBankAccDto.add(bankAccDto);
    		}
    		
    		return listBankAccDto;
    }

    public BankAccountResponceDto getBankAccountById(Long bankAccountId, Principal principal) {
    	
     	User user = userRepository.findByEmail(principal.getName())
				.orElseThrow(()-> new RuntimeException("User not found"));
    		
        BankAccount bankAcc =  bankAccountRepository.findByIdAndUser(bankAccountId, user)
                .orElseThrow(() -> new RuntimeException("Bank account not found or access denied"));
        
        BankAccountResponceDto bankAccDto = new BankAccountResponceDto();
		BeanUtils.copyProperties(bankAcc, bankAccDto);
		return bankAccDto;
    }
}
