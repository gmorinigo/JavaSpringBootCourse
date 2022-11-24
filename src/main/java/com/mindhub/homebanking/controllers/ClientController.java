package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.RoleType;
import com.mindhub.homebanking.services.IClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    IClientServices clientServices;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientServices.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @GetMapping("clients/{clientId}")
    public ClientDTO getClient(@PathVariable Long clientId){
        Client aClient = clientServices.findById(clientId);
        if (aClient != null)
            return new ClientDTO(aClient);
        else
            return null;
    }

    @GetMapping("clients/byfirstname/{firstName}")
    public List<ClientDTO> getClientsByFirstName (@PathVariable String firstName){
        return clientServices.findByFirstName(firstName);
    }

    @GetMapping("clients/byfirstname/{firstName}/{email}")
    public List<ClientDTO> getClientsByFirstNameAndEmail (@PathVariable String firstName,@PathVariable String email){
        return clientServices.findByFirstNameAndEmail(firstName,email);
    }

    @GetMapping("clients/bylastname/{lastname}")
    public List<ClientDTO> getClientsByLastName (@PathVariable String lastname){
        return clientServices.findByLastName(lastname);
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> registerClient(@RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Client aClient = clientServices.findByEmail(email);

        if (aClient !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        clientServices.createClient(firstName, lastName, email, password);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getInfoCurrentClient(Authentication authentication){
        Client aClient = clientServices.findByEmail(authentication.getName());
        if (aClient != null)
            return new ClientDTO(aClient);
        else
            return null;
    }

    @GetMapping("/clients/current/roles")
    public void get(Authentication authentication, HttpServletResponse response) throws IOException {
        Client aClient = clientServices.findByEmail(authentication.getName());

        if (aClient != null) {
            if (aClient.getRoles().contains(RoleType.ADMIN))
                response.sendRedirect("/web/test/clients.html");
            else
                response.sendRedirect("/web/accounts.html");
        }
    }
}
