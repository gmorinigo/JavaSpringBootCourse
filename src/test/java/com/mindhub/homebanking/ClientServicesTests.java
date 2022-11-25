package com.mindhub.homebanking;


import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.RoleType;
import com.mindhub.homebanking.repository.ClientRepository;
import com.mindhub.homebanking.services.IClientServices;
import com.mindhub.homebanking.services.impl.ClientServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientServicesTests {

    ClientRepository clientRepository = mock(ClientRepository.class);

    IClientServices clientServices = new ClientServicesImpl(clientRepository);

    @MockBean
    PasswordEncoder passwordEncoder;

    List<Client> mockClients;

    @BeforeEach
    public void beforeTest(){
        when(passwordEncoder.encode("123")).thenReturn("312");;

        this.mockClients = Arrays.asList(new Client("Melba", "Morel","melba@mindhub.com",
                        passwordEncoder.encode("123"), List.of(RoleType.USER)),
                new Client("Gus", "Tavo","mail@gmail.com",passwordEncoder.encode("123")
                        ,List.of(RoleType.USER)),
                new Client("admin", "admin","admin@admin.com",
                        passwordEncoder.encode("123"),List.of(RoleType.ADMIN, RoleType.USER)));
    }

    @Test
    public void findByEmailTets(){
        when(clientRepository.findByEmail("mail@gmail.com")).thenReturn(Optional.of(mockClients.get(1)));

        Client client = clientServices.findByEmail("mail@gmail.com");
        assertEquals(mockClients.get(1).getEmail(), client.getEmail());
    }
}
