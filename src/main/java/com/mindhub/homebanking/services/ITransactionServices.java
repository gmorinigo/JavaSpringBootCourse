package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.TransactionType;

import java.util.List;

public interface ITransactionServices {
    List<TransactionDTO> findByDateBetween(String dateFrom, String dateTo);

    List<TransactionDTO> findByAmountGreaterThanAndAmountLessThan(double amountFrom, double amountTo);

    List<TransactionDTO> findByType(TransactionType type);

    void makeTransaction(double amount, String description, Account originAccount, Account destinationAccount);

    void generateTransaction(TransactionType type, double amount, String description, Account destinationAccount);
}
