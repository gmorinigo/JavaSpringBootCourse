package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ICardServices {
    void createCard (Client aClient, CardType cardType, CardColor cardColor);

    List<CardDTO> findByClientDTO(Client client);
}
