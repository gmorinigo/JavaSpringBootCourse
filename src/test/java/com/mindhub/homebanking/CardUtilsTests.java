package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardUtilsTests {
    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    public void generateRandomNumber(){
        int randomNumber = CardUtils.getRandomNumber(1,10);
        assertThat(randomNumber,is(not(0)));
    }

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.generateRandomCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void randomCVVIsCreated(){
        int randomCVV = CardUtils.generateRandomCVV();
        assertThat(randomCVV,is(not(0)));
    }
}
