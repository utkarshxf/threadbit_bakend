package com.backend.threadbit.service;

import com.backend.threadbit.dto.BankAccountDTO;
import com.backend.threadbit.model.BankAccount;

import java.util.List;

public interface BankAccountService {
    
    BankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO, String userId);
    
    BankAccountDTO updateBankAccount(String id, BankAccountDTO bankAccountDTO, String userId);
    
    void deleteBankAccount(String id, String userId);
    
    BankAccountDTO getBankAccountById(String id, String userId);
    
    List<BankAccountDTO> getAllBankAccountsByUserId(String userId);
    
    BankAccountDTO setPrimaryBankAccount(String id, String userId);
    
    BankAccount getBankAccountEntityById(String id, String userId);
    
    BankAccount getPrimaryBankAccount(String userId);
}