package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface ILoanServices {
    List<LoanDTO> getAllLoans();

    boolean isValidLoan(String loanName);

    Loan findByName(String loanName);

    Loan findById(Long loanId);

}
