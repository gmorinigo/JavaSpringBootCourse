package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CardApplicationDTO;
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

import java.time.format.DateTimeFormatter;
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
           return new ResponseEntity<>("Maximum number of cards " + cardType + " exceeded"
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

    @PostMapping("/clients/validate/card")
    public ResponseEntity<Object> validateClientCard (@RequestBody CardApplicationDTO paymentCard) {
        Client loggedClient = clientServices.findByEmail(paymentCard.getEmail());
        List<CardDTO> activeCards = cardServices.activeCards(loggedClient);
        /*System.out.println("email: " + paymentCard.getEmail());
        System.out.println("numTarjeta: " + paymentCard.getNumTarjeta());
        System.out.println("cvv: " + paymentCard.getCvv());
        System.out.println("anio: " + paymentCard.getAnioVencimiento());
        System.out.println("mes: " + paymentCard.getMesVencimiento());*/
        for (CardDTO card: activeCards) {
            String paymentThruDate = paymentCard.getMesVencimiento().toString() + paymentCard.getAnioVencimiento().toString();
            String cardNumber = card.getNumber().replace("-"," ");
            String paymentNumber = paymentCard.getNumTarjeta();
            /*System.out.println(" ");
            System.out.println(cardNumber.equals(paymentNumber));
            System.out.println(card.getCvv().equals(String.format("%0" + 3 + "d", paymentCard.getCvv())));
            System.out.println(card.getThruDate().format(DateTimeFormatter.ofPattern("MMyy")).equals(paymentThruDate));
            System.out.println("cardnumber: " + cardNumber);
            System.out.println("paymentNumber: " + paymentNumber);
            System.out.println("card.cvv: " + card.getCvv());
            System.out.println("paymentCard.cvv: " + String.format("%0" + 3 + "d", paymentCard.getCvv()));
            System.out.println("paymentThruDate: " + paymentThruDate);
            System.out.println("card.thrudate: " + card.getThruDate().format(DateTimeFormatter.ofPattern("MMyy")));*/
            if (cardNumber.equals(paymentNumber)
                    && card.getCvv().equals(String.format("%0" + 3 + "d", paymentCard.getCvv()))
                    && card.getThruDate().format(DateTimeFormatter.ofPattern("MMyy")).equals(paymentThruDate)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
        //return new ResponseEntity<>("Invalid card", HttpStatus.OK);
    }
}
