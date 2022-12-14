package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.RoleType;
import com.mindhub.homebanking.repository.ClientRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {

    ClientRepository clientRepository = mock(ClientRepository.class);

    @MockBean
    PasswordEncoder passwordEncoder;

    List<Client> mockClients;

    // cada vez que se ejecute un test
    // @BeforeEach
    // 1 vez al principio
    @BeforeEach
    public void beforeTest(){
        when(passwordEncoder.encode("123")).thenReturn("312");;

        this.mockClients = Arrays.asList(new Client("Melba", "Morel","melba@mindhub.com",
                passwordEncoder.encode("123"),List.of(RoleType.USER)),
                new Client("Gus", "Tavo","mail@gmail.com",passwordEncoder.encode("123")
                        ,List.of(RoleType.USER)),
                new Client("admin", "admin","admin@admin.com",
                        passwordEncoder.encode("123"),List.of(RoleType.ADMIN, RoleType.USER)));
    }

    @Test
    public void existClientsInDB (){
        when(clientRepository.findAll()).thenReturn(mockClients);
        List<Client> clients = clientRepository.findAll();

        assertThat(clients, hasItem(hasProperty("firstName", is("Gus"))));
        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));
    }

    @Test
    public void existOnlyOneAdmin (){
        when(clientRepository.findAll()).thenReturn(mockClients);
        List<Client> clients = clientRepository.findAll().stream().filter(client -> client.getRoles().contains(RoleType.ADMIN))
                .collect(Collectors.toList());

        assertThat (clients.size(), equalTo(1));
    }
}

