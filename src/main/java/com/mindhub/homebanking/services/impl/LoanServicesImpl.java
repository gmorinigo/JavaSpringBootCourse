package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repository.LoanRepository;
import com.mindhub.homebanking.services.ILoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServicesImpl implements ILoanServices {
    @Autowired
    LoanRepository loanRepository;

    @Override
    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public boolean isValidLoan(String loanName) {
        return (loanRepository.findByName(loanName) != null);
    }

    @Override
    public Loan findByName(String loanName) {
        return loanRepository.findByName(loanName);
    }

    @Override
    public Loan findById(Long loanId) {
        return loanRepository.findById(loanId).orElse(null);
    }

}
