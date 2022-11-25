package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.CardRepository;
import com.mindhub.homebanking.services.ICardServices;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CardServicesImpl implements ICardServices {
    @Autowired
    CardRepository cardRepository;

    @Override
    public void createCard(Client aClient, CardType cardType, CardColor cardColor) {
        LocalDate localDate = LocalDate.now();
        Card cardToCreate = new Card(aClient.getFirstName() + " " + aClient.getLastName(),cardType, cardColor,
                CardUtils.generateRandomCardNumber(), CardUtils.generateRandomCVV(), localDate,
                localDate.plusYears(5), aClient);

        cardRepository.save(cardToCreate);
    }

    @Override
    public List<CardDTO> findByClientDTO(Client client) {
        return cardRepository.findByClient(client).stream().map(CardDTO::new).collect(toList());
    }

    @Override
    public Card findByNumber(String cardNumber) {
        return cardRepository.findByNumber(cardNumber);
    }

    @Override
    public void deleteCard(Card card) {
        card.setActiveCard(false);
        cardRepository.save(card);
    }
}
