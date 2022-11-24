package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardRepositoryTest {
    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    CardRepository cardRepository;

    @Test
    public void existDebitAndCreditCards (){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("type", is(CardType.DEBIT))));
        assertThat(cards, hasItem(hasProperty("type", is(CardType.CREDIT))));
    }

    @Test
    public void existCardsWithSilverColor (){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("color", is(CardColor.SILVER))));
    }
}
