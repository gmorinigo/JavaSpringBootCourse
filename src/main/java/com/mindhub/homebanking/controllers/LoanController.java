package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.LoanRequestDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.services.IAccountServices;
import com.mindhub.homebanking.services.IClientLoanServices;
import com.mindhub.homebanking.services.IClientServices;
import com.mindhub.homebanking.services.ILoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    ILoanServices loanService;

    @Autowired
    IClientServices clientServices;

    @Autowired
    IAccountServices accountServices;

    @Autowired
    IClientLoanServices clientLoanServices;

    @Transactional
    @GetMapping("/loans")
    public List<LoanDTO> loanRequest (){
        return loanService.getAllLoans();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> loanRequest (@RequestBody LoanRequestDTO loanRequestDTO, Authentication authentication){
        if (loanRequestDTO.hasMissingData()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Client clientAutenthicated = clientServices.findByEmail(authentication.getName());
        if (clientAutenthicated ==  null)
            return new ResponseEntity<>("Client Not Found", HttpStatus.NOT_FOUND);

        Loan loan = loanService.findById(loanRequestDTO.getLoanId());
        if (loan == null)
            return new ResponseEntity<>("Loan Not Found", HttpStatus.NOT_FOUND);

        if (loanRequestDTO.getAmount() > loan.getMaxAmount())
            return new ResponseEntity<>("Amount of the loan request exceeded the maximun allowed", HttpStatus.FORBIDDEN);

        if (!loan.getPayments().contains(loanRequestDTO.getPayments()))
            return new ResponseEntity<>("Payments quantity invalid", HttpStatus.FORBIDDEN);

        Account destinationAccount = accountServices.findByAccountByNumber(loanRequestDTO.getToAccountNumber());
        if (destinationAccount == null)
            return new ResponseEntity<>("Destination Account Not Found", HttpStatus.NOT_FOUND);

        if (!destinationAccount.getClient().equals(clientAutenthicated))
            return new ResponseEntity<>("Destination Account is not of the client Authenticated", HttpStatus.FORBIDDEN);

        clientLoanServices.loanAcreditation(clientAutenthicated, loanRequestDTO, loan, destinationAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
