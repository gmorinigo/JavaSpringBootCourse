package com.mindhub.homebanking.dtos;

public class CardApplicationDTO {
    private String email;
    private String numTarjeta;
    private Integer cvv;
    private Integer anioVencimiento;
    private Integer mesVencimiento;

    public CardApplicationDTO() {
    }

    public CardApplicationDTO(String email, String numTarjeta, Integer cvv, Integer anioVencimiento, Integer mesVencimiento) {
        this.numTarjeta = numTarjeta;
        this.cvv = cvv;
        this.anioVencimiento = anioVencimiento;
        this.mesVencimiento = mesVencimiento;
        this.email = email;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public Integer getCvv() {
        return cvv;
    }

    public Integer getAnioVencimiento() { return anioVencimiento; }

    public Integer getMesVencimiento() {
        return mesVencimiento;
    }

    public String getEmail() { return email; }
}
