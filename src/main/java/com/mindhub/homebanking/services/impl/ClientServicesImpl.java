package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.RoleType;
import com.mindhub.homebanking.repository.ClientRepository;
import com.mindhub.homebanking.services.IAccountServices;
import com.mindhub.homebanking.services.IClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServicesImpl implements IClientServices {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    IAccountServices accountServices;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ClientServicesImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Client findById(Long clientid) {
        return clientRepository.findById(clientid).orElse(null);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public List<ClientDTO> findByFirstName(String firstName) {
        return clientRepository.findByFirstName(firstName).stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public List<ClientDTO> findByFirstNameAndEmail(String firstName, String email) {
        return clientRepository.findByFirstNameAndEmail(firstName,email).stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public List<ClientDTO> findByLastName(String lastname) {
        return clientRepository.findByLastName(lastname).stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public void createClient(String firstName, String lastName, String email, String password) {
        Client clientCreated = new Client(firstName, lastName, email, passwordEncoder.encode(password),
                List.of(RoleType.USER));

        clientRepository.save(clientCreated);

        accountServices.generateAccount(clientCreated);
    }
}
