package com.backend.threadbit.service;

import com.backend.threadbit.dto.BankAccountDTO;
import com.backend.threadbit.execption.ResourceNotFoundException;
import com.backend.threadbit.model.BankAccount;
import com.backend.threadbit.model.User;
import com.backend.threadbit.repository.BankAccountRepository;
import com.backend.threadbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if account number already exists for this user (only for bank accounts)
        if (bankAccountDTO.getAccountType() == BankAccountDTO.AccountType.BANK_ACCOUNT && 
            bankAccountDTO.getAccountNumber() != null && 
            bankAccountRepository.existsByUserIdAndAccountNumber(userId, bankAccountDTO.getAccountNumber())) {
            throw new IllegalArgumentException("Bank account with this account number already exists for this user");
        }

        BankAccount.BankAccountBuilder builder = BankAccount.builder()
                .accountType(bankAccountDTO.getAccountType())
                .userId(userId)
                .user(user)
                .isActive(true)
                .isPrimary(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());

        // Set fields based on account type
        if (bankAccountDTO.getAccountType() == BankAccountDTO.AccountType.BANK_ACCOUNT) {
            builder.accountNumber(bankAccountDTO.getAccountNumber())
                   .accountHolderName(bankAccountDTO.getAccountHolderName())
                   .bankName(bankAccountDTO.getBankName())
                   .ifscCode(bankAccountDTO.getIfscCode());
        } else if (bankAccountDTO.getAccountType() == BankAccountDTO.AccountType.UPI) {
            builder.upiId(bankAccountDTO.getUpiId());
        }

        BankAccount bankAccount = builder.build();

        // If this is the first account, make it primary
        if (bankAccountRepository.findByUserId(userId).isEmpty()) {
            bankAccount.setPrimary(true);
        }

        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        return mapToDTO(savedBankAccount);
    }

    @Override
    @Transactional
    public BankAccountDTO updateBankAccount(String id, BankAccountDTO bankAccountDTO, String userId) {
        BankAccount bankAccount = getBankAccountEntityById(id, userId);

        // Update account type
        bankAccount.setAccountType(bankAccountDTO.getAccountType());

        // Update fields based on account type
        if (bankAccountDTO.getAccountType() == BankAccountDTO.AccountType.BANK_ACCOUNT) {
            bankAccount.setAccountNumber(bankAccountDTO.getAccountNumber());
            bankAccount.setAccountHolderName(bankAccountDTO.getAccountHolderName());
            bankAccount.setBankName(bankAccountDTO.getBankName());
            bankAccount.setIfscCode(bankAccountDTO.getIfscCode());
            bankAccount.setUpiId(null); // Clear UPI ID for bank account
        } else if (bankAccountDTO.getAccountType() == BankAccountDTO.AccountType.UPI) {
            bankAccount.setUpiId(bankAccountDTO.getUpiId());
            bankAccount.setAccountNumber(null); // Clear bank account details for UPI
            bankAccount.setAccountHolderName(null);
            bankAccount.setBankName(null);
            bankAccount.setIfscCode(null);
        }

        bankAccount.setActive(bankAccountDTO.isActive());
        bankAccount.setUpdatedAt(LocalDateTime.now());

        BankAccount updatedBankAccount = bankAccountRepository.save(bankAccount);
        return mapToDTO(updatedBankAccount);
    }

    @Override
    @Transactional
    public void deleteBankAccount(String id, String userId) {
        BankAccount bankAccount = getBankAccountEntityById(id, userId);

        // If this is the primary account, throw an exception
        if (bankAccount.isPrimary()) {
            throw new IllegalStateException("Cannot delete the primary bank account. Set another account as primary first.");
        }

        bankAccountRepository.delete(bankAccount);
    }

    @Override
    public BankAccountDTO getBankAccountById(String id, String userId) {
        return mapToDTO(getBankAccountEntityById(id, userId));
    }

    @Override
    public List<BankAccountDTO> getAllBankAccountsByUserId(String userId) {
        return bankAccountRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BankAccountDTO setPrimaryBankAccount(String id, String userId) {
        BankAccount newPrimaryAccount = getBankAccountEntityById(id, userId);

        // Find the current primary account and unset it
        bankAccountRepository.findByUserIdAndIsPrimaryTrue(userId).ifPresent(currentPrimary -> {
            currentPrimary.setPrimary(false);
            bankAccountRepository.save(currentPrimary);
        });

        // Set the new primary account
        newPrimaryAccount.setPrimary(true);
        newPrimaryAccount.setUpdatedAt(LocalDateTime.now());

        BankAccount updatedBankAccount = bankAccountRepository.save(newPrimaryAccount);
        return mapToDTO(updatedBankAccount);
    }

    @Override
    public BankAccount getBankAccountEntityById(String id, String userId) {
        return bankAccountRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + id));
    }

    @Override
    public BankAccount getPrimaryBankAccount(String userId) {
        return bankAccountRepository.findByUserIdAndIsPrimaryTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No primary bank account found for user: " + userId));
    }

    private BankAccountDTO mapToDTO(BankAccount bankAccount) {
        return BankAccountDTO.builder()
                .id(bankAccount.getId())
                .accountType(bankAccount.getAccountType())
                .accountNumber(bankAccount.getAccountNumber())
                .accountHolderName(bankAccount.getAccountHolderName())
                .bankName(bankAccount.getBankName())
                .ifscCode(bankAccount.getIfscCode())
                .upiId(bankAccount.getUpiId())
                .isActive(bankAccount.isActive())
                .isPrimary(bankAccount.isPrimary())
                .build();
    }
}
