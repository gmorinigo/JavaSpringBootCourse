package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repository.TransactionRepository;
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
public class TransacctionRepositoryTest {
    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existTransactionWithTypeDebit (){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,hasItem(hasProperty("type", is(TransactionType.CREDIT))));
    }

    @Test
    public void existTransactionsWithAmountGreatherThanTenThousands (){
        List<Transaction> transactions = transactionRepository.findByAmountGreaterThanAndAmountLessThan(10000,900000);
        assertThat(transactions.size(),equalTo(3));
    }
}
