package com.mindhub.homebanking.repository;

import com.mindhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan,Long> {
    Loan findByName(String loanName);
    Optional<Loan> findById(Long id);

}
