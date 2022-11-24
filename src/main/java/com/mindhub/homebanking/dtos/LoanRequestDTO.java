package com.mindhub.homebanking.dtos;

public class LoanRequestDTO {
    private Long loanId;
    private double amount;
    private int payments;
    private String toAccountNumber;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanName(Long loanId) {
        this.loanId = loanId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public boolean hasMissingData() {
        return (this.loanId == 0 || this.amount == 0 || this.payments == 0 || this.toAccountNumber == null);
    }
}
