package com.mindhub.homebanking.repository;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client,Long> {

    public List<Client> findByFirstName(String firstName);

    public List<Client> findByFirstNameAndEmail(String firstName, String email);

    public List<Client> findByLastName(String lastname);

    public Optional<Client> findByEmail(String email);
}
