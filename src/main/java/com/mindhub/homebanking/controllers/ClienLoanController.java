package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.services.IClientLoanServices;
import com.mindhub.homebanking.services.IClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/api")
public class ClienLoanController {

    @Autowired
    IClientLoanServices clientLoanServices;

    @Autowired
    IClientServices clientServices;

    @GetMapping("/clientloans/searchbyidclient/{clientid}")
    public List<ClientLoanDTO> getClientLoansByClientId(@PathVariable Long clientid){
        Client aClient = clientServices.findById(clientid);
        return clientLoanServices.findByClient(aClient).stream().map(ClientLoanDTO::new).collect(toList());
    }

    @GetMapping("/clientloans/amountgreaterthan/{amount}")
    public List<ClientLoanDTO> getClientLoansByAmountGreaterThan(@PathVariable double amount){
        return clientLoanServices.findByAmountGreaterThan(amount);

    }

    @GetMapping("/clientloans/clientsloansoffclientswithaccountamountgreatherthan/{amount}")
    public List<ClientLoanDTO> getClientsLoansOffClientsWithAccountAmountGreatherThan(@PathVariable double amount){
        // Obtengo todos los clientes y filtro los que tengan cuentas con importe mayor al parametro
        List<Client> clients = clientServices.findAll();
        Set <Account> accounts = new HashSet<>();
        for (Client client : clients) {
            accounts.addAll(client.getAccounts().stream().filter(acc -> acc.getBalance() > amount).collect(toSet()));
        }

        // Con las cuentas que tenía obtengo los clientes y luego los prestamos de esos clientes
        Set<ClientLoan> clientLoansSelected = new HashSet<>();
        for (Account account: accounts){
            clientLoansSelected.addAll(clientLoanServices.findByClient(account.getClient()).stream().collect(toSet()));
        }

        // Mapeo los clientLoans a ClientLoanDTO
        return clientLoansSelected.stream().map(ClientLoanDTO::new).collect(toList());
    }
}
