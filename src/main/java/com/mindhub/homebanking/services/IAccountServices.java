package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface IAccountServices {
    List<AccountDTO> findAll();

    AccountDTO findById(Long accountId);

    List<AccountDTO> findByBalanceGreaterThan(double balance);

    List<AccountDTO> findByCreationDateLessThan(String date);

    AccountDTO findByNumber(String number);

    void generateAccount(Client aClient);

    Account findByAccountByNumber(String accountNumber);

    void substractAmount(double amount, Account originAccount);

    void addAmount(double amount, Account destinationAccount);

    List<AccountDTO> findByClientDTO(Client client);
}
