package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface IClientServices {
    Client findByEmail(String email);

    Client findById(Long clientid);

    List<Client> findAll();

    List<ClientDTO> findByFirstName(String firstName);

    List<ClientDTO> findByFirstNameAndEmail(String firstName, String email);

    List<ClientDTO> findByLastName(String lastname);

    void createClient(String firstName, String lastName, String email, String password);
}
