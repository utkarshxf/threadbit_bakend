package com.backend.threadbit.controller;

import com.backend.threadbit.dto.BankAccountDTO;
import com.backend.threadbit.dto.CreditRequestDTO;
import com.backend.threadbit.dto.DebitRequestDTO;
import com.backend.threadbit.dto.TransactionDTO;
import com.backend.threadbit.model.Transaction;
import com.backend.threadbit.service.BankAccountService;
import com.backend.threadbit.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/bank-accounts")
@Slf4j
@CrossOrigin(
        origins = {"https://threadbitwebsite-fqj4l.ondigitalocean.app" , "https://threadbit.in" , "threadbit.in","www.threadbit.in", "https://threadbid.in" , "threadbid.in","www.threadbid.in","http://192.168.32.1:5173", "https://secondhand-threads.vercel.app","http://10.244.72.46:8080"},
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    // Bank Account CRUD Operations
    
    @PostMapping
    public ResponseEntity<BankAccountDTO> createBankAccount(
            @Valid @RequestBody BankAccountDTO bankAccountDTO,
            @RequestParam String userId) {
        return new ResponseEntity<>(bankAccountService.createBankAccount(bankAccountDTO, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> getBankAccountById(
            @PathVariable String id,
            @RequestParam String userId) {
        return ResponseEntity.ok(bankAccountService.getBankAccountById(id, userId));
    }

    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccountsByUserId(
            @RequestParam String userId) {
        return ResponseEntity.ok(bankAccountService.getAllBankAccountsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccountDTO> updateBankAccount(
            @PathVariable String id,
            @Valid @RequestBody BankAccountDTO bankAccountDTO,
            @RequestParam String userId) {
        return ResponseEntity.ok(bankAccountService.updateBankAccount(id, bankAccountDTO, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankAccount(
            @PathVariable String id,
            @RequestParam String userId) {
        bankAccountService.deleteBankAccount(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/set-primary")
    public ResponseEntity<BankAccountDTO> setPrimaryBankAccount(
            @PathVariable String id,
            @RequestParam String userId) {
        return ResponseEntity.ok(bankAccountService.setPrimaryBankAccount(id, userId));
    }

    // Transaction Operations
    
    @PostMapping("/credit")
    public ResponseEntity<TransactionDTO> creditAmount(
            @Valid @RequestBody CreditRequestDTO creditRequestDTO) {
        return new ResponseEntity<>(transactionService.creditAmount(creditRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/debit")
    public ResponseEntity<TransactionDTO> debitAmount(
            @Valid @RequestBody DebitRequestDTO debitRequestDTO) {
        return new ResponseEntity<>(transactionService.debitAmount(debitRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(
            @PathVariable String id,
            @RequestParam String userId) {
        return ResponseEntity.ok(transactionService.getTransactionById(id, userId));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsByUserId(
            @RequestParam String userId) {
        return ResponseEntity.ok(transactionService.getAllTransactionsByUserId(userId));
    }

    @GetMapping("/transactions/paged")
    public ResponseEntity<Page<TransactionDTO>> getPagedTransactionsByUserId(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        return ResponseEntity.ok(transactionService.getAllTransactionsByUserId(userId, pageable));
    }

    @GetMapping("/transactions/type/{type}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByType(
            @PathVariable Transaction.TransactionType type,
            @RequestParam String userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUserIdAndType(userId, type));
    }

    @GetMapping("/transactions/type/{type}/paged")
    public ResponseEntity<Page<TransactionDTO>> getPagedTransactionsByType(
            @PathVariable Transaction.TransactionType type,
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        return ResponseEntity.ok(transactionService.getTransactionsByUserIdAndType(userId, type, pageable));
    }

    @GetMapping("/transactions/date-range")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDateRange(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        
        return ResponseEntity.ok(transactionService.getTransactionsByUserIdAndDateRange(userId, start, end));
    }

    @GetMapping("/transactions/date-range/paged")
    public ResponseEntity<Page<TransactionDTO>> getPagedTransactionsByDateRange(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        return ResponseEntity.ok(transactionService.getTransactionsByUserIdAndDateRange(userId, start, end, pageable));
    }
}