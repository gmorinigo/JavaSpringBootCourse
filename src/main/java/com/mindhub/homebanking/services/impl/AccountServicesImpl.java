package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.AccountRepository;
import com.mindhub.homebanking.services.IAccountServices;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServicesImpl implements IAccountServices {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<AccountDTO> findAll() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @Override
    public AccountDTO findById(Long accountId) {
        return accountRepository.findById(accountId).map(AccountDTO::new).orElse(null);
    }

    @Override
    public List<AccountDTO> findByBalanceGreaterThan(double balance) {
        return accountRepository.findByBalanceGreaterThan(balance).stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public List<AccountDTO> findByCreationDateLessThan(String date) {
        return accountRepository.findByCreationDateLessThan(LocalDate.parse(date).atStartOfDay()).stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public AccountDTO findByNumber(String number) {
        return accountRepository.findByNumber(number).map(AccountDTO::new).orElse(null);
        //if (anAccount != null) return new AccountDTO(anAccount);
        //else return null;
    }

    @Override
    public void generateAccount(Client aClient) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        Account anAccount = new Account(this.generateAccountNumber(), dateTimeNow,0, aClient);
        accountRepository.save(anAccount);
    }

    @Override
    public Account findByAccountByNumber(String AccountNumber) {
        return accountRepository.findByNumber(AccountNumber).orElse(null);
    }

    @Override
    public void substractAmount(double amount, Account account) {
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @Override
    public void addAmount(double amount, Account account) {
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> findByClientDTO(Client clientAuthenticated) {
        return accountRepository.findByClient(clientAuthenticated).stream().map(AccountDTO::new).collect(toList());
    }

    public String generateAccountNumber() {
        boolean accountValid = false;
        String accountNumber = "";

        while (!accountValid) {
            int randomNumber = CardUtils.getRandomNumber(1,99999999);
            accountNumber = ("VIN-" + String.format("%0" + 8 + "d",randomNumber));
            if (accountRepository.findByNumber(accountNumber).orElse(null) == null)
                accountValid = true;
        }
        return accountNumber;
    }
}
