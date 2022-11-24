package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanRequestDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.ClientLoanRepository;
import com.mindhub.homebanking.repository.TransactionRepository;
import com.mindhub.homebanking.services.IAccountServices;
import com.mindhub.homebanking.services.IClientLoanServices;
import com.mindhub.homebanking.services.ITransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientLoanServicesImpl implements IClientLoanServices {
    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    ITransactionServices transactionServices;

    @Autowired
    IAccountServices accountServices;

    @Override
    public List<ClientLoan> findByClient(Client aClient) {
        return clientLoanRepository.findByClient(aClient);
    }

    @Override
    public List<ClientLoanDTO> findByAmountGreaterThan(double amount) {
        return clientLoanRepository.findByAmountGreaterThan(amount).stream().map(ClientLoanDTO::new).collect(toList());
    }

    @Override
    public void loanAcreditation(Client client, LoanRequestDTO loanRequestDTO, Loan loan, Account destinationAccount) {
        double amountWithFee = loanRequestDTO.getAmount() * 1.20;
        ClientLoan clientLoan = new ClientLoan(amountWithFee,loanRequestDTO.getPayments(),
                client, loan);
        clientLoanRepository.save(clientLoan);

        transactionServices.generateTransaction(TransactionType.CREDIT,loanRequestDTO.getAmount()
                ,"Loan " + clientLoan.getLoan().getName() + " Acreditation", destinationAccount);

        accountServices.addAmount(loanRequestDTO.getAmount(), destinationAccount);

    }
}
