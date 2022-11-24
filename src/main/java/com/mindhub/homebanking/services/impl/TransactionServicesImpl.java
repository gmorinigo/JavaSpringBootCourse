package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repository.AccountRepository;
import com.mindhub.homebanking.repository.TransactionRepository;
import com.mindhub.homebanking.services.IAccountServices;
import com.mindhub.homebanking.services.ITransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class TransactionServicesImpl implements ITransactionServices {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    IAccountServices accountServices;

    @Override
    public List<TransactionDTO> findByDateBetween(String dateFrom, String dateTo) {
        return transactionRepository.findByDateBetween(LocalDate.parse(dateFrom).atStartOfDay()
                        ,LocalDate.parse(dateTo).atStartOfDay())
                .stream().map(TransactionDTO::new).collect(Collectors.toList());    }

    @Override
    public List<TransactionDTO> findByAmountGreaterThanAndAmountLessThan(double amountFrom, double amountTo) {
        return transactionRepository.findByAmountGreaterThanAndAmountLessThan(amountFrom,amountTo).stream().map(TransactionDTO::new).collect(toList());
    }

    @Override
    public List<TransactionDTO> findByType(TransactionType type) {
        return transactionRepository.findByType(type).stream().map(TransactionDTO::new).collect(toList());
    }

    @Override
    public void makeTransaction(double amount, String description, Account originAccount, Account destinationAccount) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        accountServices.substractAmount(amount, originAccount);
        accountServices.addAmount(amount, destinationAccount);
        transactionRepository.save(new Transaction(TransactionType.DEBIT,amount*-1, description, dateTimeNow, originAccount));
        transactionRepository.save(new Transaction(TransactionType.CREDIT,amount, description, dateTimeNow, destinationAccount));
    }

    @Override
    public void generateTransaction(TransactionType type, double amount, String description, Account destinationAccount) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        transactionRepository.save(new Transaction(type,amount, description, dateTimeNow, destinationAccount));
    }
}
