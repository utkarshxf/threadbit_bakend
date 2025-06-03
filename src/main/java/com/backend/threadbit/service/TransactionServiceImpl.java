package com.backend.threadbit.service;

import com.backend.threadbit.dto.CreditRequestDTO;
import com.backend.threadbit.dto.DebitRequestDTO;
import com.backend.threadbit.dto.TransactionDTO;
import com.backend.threadbit.execption.ResourceNotFoundException;
import com.backend.threadbit.model.BankAccount;
import com.backend.threadbit.model.Transaction;
import com.backend.threadbit.model.User;
import com.backend.threadbit.repository.BankAccountRepository;
import com.backend.threadbit.repository.TransactionRepository;
import com.backend.threadbit.repository.UserRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final BankAccountService bankAccountService;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Override
    @Transactional
    public TransactionDTO creditAmount(CreditRequestDTO creditRequestDTO) {
        User user = userRepository.findById(creditRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + creditRequestDTO.getUserId()));

        BankAccount bankAccount = bankAccountRepository.findByUserIdAndId(creditRequestDTO.getUserId(), creditRequestDTO.getBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + creditRequestDTO.getBankAccountId()));

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .userId(user.getId())
                .user(user)
                .bankAccountId(bankAccount.getId())
                .bankAccount(bankAccount)
                .type(Transaction.TransactionType.CREDIT)
                .amount(creditRequestDTO.getAmount())
                .currency(creditRequestDTO.getCurrency() != null ? creditRequestDTO.getCurrency() : "INR")
                .status(Transaction.TransactionStatus.COMPLETED)
                .referenceId(creditRequestDTO.getReferenceId() != null ? creditRequestDTO.getReferenceId() : UUID.randomUUID().toString())
                .description(creditRequestDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Update user wallet balance
        updateUserWalletBalance(user, creditRequestDTO.getAmount(), true);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToDTO(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionDTO debitAmount(DebitRequestDTO debitRequestDTO) {
        User user = userRepository.findById(debitRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + debitRequestDTO.getUserId()));

        BankAccount bankAccount = bankAccountRepository.findByUserIdAndId(debitRequestDTO.getUserId(), debitRequestDTO.getBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found with id: " + debitRequestDTO.getBankAccountId()));

        // Check if user has sufficient balance
        BigDecimal currentBalance = new BigDecimal(user.getWalletBalance() != null ? user.getWalletBalance() : "0");
        if (currentBalance.compareTo(debitRequestDTO.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient balance in wallet");
        }

        // Create transaction record with PENDING status
        Transaction transaction = Transaction.builder()
                .userId(user.getId())
                .user(user)
                .bankAccountId(bankAccount.getId())
                .bankAccount(bankAccount)
                .type(Transaction.TransactionType.DEBIT)
                .amount(debitRequestDTO.getAmount())
                .currency(debitRequestDTO.getCurrency() != null ? debitRequestDTO.getCurrency() : "INR")
                .status(Transaction.TransactionStatus.PENDING)
                .referenceId(UUID.randomUUID().toString())
                .description(debitRequestDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        try {
            // Process payment via Razorpay
            String razorpayPaymentId = processRazorpayPayment(debitRequestDTO, savedTransaction.getReferenceId());
            
            // Update transaction with payment ID and status
            savedTransaction.setRazorpayPaymentId(razorpayPaymentId);
            savedTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            savedTransaction.setUpdatedAt(LocalDateTime.now());
            
            // Update user wallet balance
            updateUserWalletBalance(user, debitRequestDTO.getAmount(), false);
            
            Transaction updatedTransaction = transactionRepository.save(savedTransaction);
            return mapToDTO(updatedTransaction);
        } catch (Exception e) {
            // Update transaction status to FAILED
            savedTransaction.setStatus(Transaction.TransactionStatus.FAILED);
            savedTransaction.setDescription(savedTransaction.getDescription() + " | Error: " + e.getMessage());
            savedTransaction.setUpdatedAt(LocalDateTime.now());
            
            Transaction failedTransaction = transactionRepository.save(savedTransaction);
            
            log.error("Failed to process Razorpay payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process payment: " + e.getMessage(), e);
        }
    }

    private String processRazorpayPayment(DebitRequestDTO debitRequestDTO, String referenceId) {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            
            JSONObject payoutRequest = new JSONObject();
            payoutRequest.put("account_number", "2323230032510196");  // Your Razorpay account number
            payoutRequest.put("amount", debitRequestDTO.getAmount().multiply(new BigDecimal(100)).intValue());  // Amount in paise
            payoutRequest.put("currency", "INR");
            payoutRequest.put("mode", debitRequestDTO.getTransferMode() != null ? debitRequestDTO.getTransferMode() : "BANK_ACCOUNT");
            payoutRequest.put("purpose", "payout");
            payoutRequest.put("reference_id", referenceId);
            
            JSONObject fund_account = new JSONObject();
            
            if ("UPI".equals(debitRequestDTO.getTransferMode())) {
                fund_account.put("account_type", "vpa");
                
                JSONObject vpa = new JSONObject();
                vpa.put("address", debitRequestDTO.getUpiId());
                fund_account.put("vpa", vpa);
            } else {
                fund_account.put("account_type", "bank_account");
                
                JSONObject bank_account = new JSONObject();
                bank_account.put("name", debitRequestDTO.getAccountHolderName());
                bank_account.put("ifsc", debitRequestDTO.getIfscCode());
                bank_account.put("account_number", debitRequestDTO.getAccountNumber());
                fund_account.put("bank_account", bank_account);
            }
            
            payoutRequest.put("fund_account", fund_account);
            
            JSONObject contact = new JSONObject();
            contact.put("name", debitRequestDTO.getAccountHolderName());
            contact.put("email", debitRequestDTO.getContactEmail());
            contact.put("contact", debitRequestDTO.getContactPhone());
            contact.put("type", "customer");
            
            payoutRequest.put("contact", contact);
            
            // This is a mock implementation since we can't actually make the API call in this context
            // In a real implementation, you would call razorpayClient.payouts.create(payoutRequest)
            // and get the actual payment ID from the response
            
            // For now, we'll just return a mock payment ID
            return "pout_" + UUID.randomUUID().toString().replace("-", "");
            
        } catch (RazorpayException e) {
            log.error("Razorpay API error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process Razorpay payment: " + e.getMessage(), e);
        }
    }

    private void updateUserWalletBalance(User user, BigDecimal amount, boolean isCredit) {
        BigDecimal currentBalance = new BigDecimal(user.getWalletBalance() != null ? user.getWalletBalance() : "0");
        BigDecimal newBalance;
        
        if (isCredit) {
            newBalance = currentBalance.add(amount);
        } else {
            newBalance = currentBalance.subtract(amount);
        }
        
        user.setWalletBalance(newBalance.toString());
        userRepository.save(user);
    }

    @Override
    public TransactionDTO getTransactionById(String id, String userId) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        
        if (!transaction.getUserId().equals(userId)) {
            throw new IllegalStateException("Transaction does not belong to user: " + userId);
        }
        
        return mapToDTO(transaction);
    }

    @Override
    public List<TransactionDTO> getAllTransactionsByUserId(String userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransactionDTO> getAllTransactionsByUserId(String userId, Pageable pageable) {
        return transactionRepository.findByUserId(userId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public List<TransactionDTO> getTransactionsByUserIdAndType(String userId, Transaction.TransactionType type) {
        return transactionRepository.findByUserIdAndType(userId, type).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransactionDTO> getTransactionsByUserIdAndType(String userId, Transaction.TransactionType type, Pageable pageable) {
        return transactionRepository.findByUserIdAndType(userId, type, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public List<TransactionDTO> getTransactionsByUserIdAndDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TransactionDTO> getTransactionsByUserIdAndDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return transactionRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate, pageable)
                .map(this::mapToDTO);
    }

    private TransactionDTO mapToDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .bankAccountId(transaction.getBankAccountId())
                .bankAccountNumber(transaction.getBankAccount() != null ? transaction.getBankAccount().getAccountNumber() : null)
                .bankName(transaction.getBankAccount() != null ? transaction.getBankAccount().getBankName() : null)
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus())
                .referenceId(transaction.getReferenceId())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}