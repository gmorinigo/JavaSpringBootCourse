package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ICardServices;
import com.mindhub.homebanking.services.IClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    ICardServices cardServices;

    @Autowired
    IClientServices clientServices;

    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> createCard (@RequestParam CardColor cardColor, @RequestParam CardType cardType,
                                              Authentication authentication){
       Client aClient = clientServices.findByEmail(authentication.getName());

       if (aClient ==  null)
           return new ResponseEntity<>("Client Not Found", HttpStatus.NOT_FOUND);

       if (aClient.getCards().stream().filter(card -> card.getType().equals(cardType) && card.isActiveCard()).count() > 2)
           return new ResponseEntity<>("Maximum number of cards " + cardType.toString() + " exceeded"
                   , HttpStatus.NOT_FOUND);

       cardServices.createCard(aClient,cardType,cardColor);

       return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/clients/current/cards")
    public List<CardDTO> getCards(Authentication authentication){
        Client clientAuthenticated = clientServices.findByEmail(authentication.getName());
        List<CardDTO> cardsOfClientAuthenticated = cardServices.findByClientDTO(clientAuthenticated).stream()
                .filter(cardDTO -> cardDTO.isActiveCard()).collect(Collectors.toList());
        return cardsOfClientAuthenticated;

    }

    @DeleteMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> getCards(Authentication authentication, @RequestParam String cardNumber){
        Client clientAuthenticated = clientServices.findByEmail(authentication.getName());
        Card card = cardServices.findByNumber(cardNumber);
        if (clientAuthenticated.getId() != card.getClient().getId())
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        cardServices.deleteCard(card);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
