package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanRequestDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface IClientLoanServices {
    List<ClientLoan> findByClient(Client aClient);

    List<ClientLoanDTO> findByAmountGreaterThan(double amount);

    void loanAcreditation(Client client, LoanRequestDTO loanRequestDTO, Loan loan, Account destinationAccount);
}
