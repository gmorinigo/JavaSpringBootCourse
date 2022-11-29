package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			Client adminClient = new Client("admin", "admin","admin@admin.com",
					passwordEncoder.encode("123"),List.of(RoleType.ADMIN, RoleType.USER));
			Client melbaClient = new Client("Melba", "Morel","melba@mindhub.com",
					passwordEncoder.encode("123"),List.of(RoleType.USER));
			Client gusTavoClient = new Client("Gus", "Tavo","mail@gmail.com",
					passwordEncoder.encode("123"),List.of(RoleType.USER));
			Client otherGusClient = new Client("Gus", "Other","othermail@gmail.com",
					passwordEncoder.encode("123"),List.of(RoleType.USER));
			clientRepository.save(adminClient);
			clientRepository.save(melbaClient);
			clientRepository.save(gusTavoClient);
			clientRepository.save(otherGusClient);

			LocalDateTime dateTimeNow = LocalDateTime.now();
			LocalDate localDate = dateTimeNow.toLocalDate();

			this.createAccountsAndTransacctions(melbaClient, gusTavoClient, dateTimeNow, accountRepository, transactionRepository);
			this.createLoans(melbaClient, gusTavoClient, loanRepository, clientLoanRepository);
			this.createCards(melbaClient, gusTavoClient,localDate, cardRepository);
		};
	}

	private void createAccountsAndTransacctions(Client melbaClient, Client gusTavoClient, LocalDateTime dateTimeNow, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		Account accountTest1 = new Account("VIN001", dateTimeNow,5000,melbaClient);
		accountRepository.save(accountTest1);
		Account accountTest2 = new Account("VIN002", dateTimeNow.plusDays(1),10000,melbaClient);
		accountRepository.save(accountTest2);
		Account accountTest3 = new Account("VIN003", dateTimeNow.plusMonths(1),20000,gusTavoClient);
		accountRepository.save(accountTest3);
		Account accountTest4 = new Account("VIN004", dateTimeNow.plusYears(1),30000,gusTavoClient);
		accountRepository.save(accountTest4);

		this.createTransactions(dateTimeNow, transactionRepository, accountTest1, accountTest2, accountTest3, accountTest4);
	}

	private void createTransactions(LocalDateTime dateTimeNow, TransactionRepository transactionRepository, Account accountTest1, Account accountTest2, Account accountTest3, Account accountTest4) {
		Transaction transaction1 = new Transaction(TransactionType.CREDIT, 100000, "Acreditacion Sueldo", dateTimeNow, accountTest1);
		Transaction transaction2 = new Transaction(TransactionType.DEBIT, -2000, "Pago Internet", dateTimeNow, accountTest1);
		Transaction transaction3 = new Transaction(TransactionType.DEBIT, -5000, "Pago Luz", dateTimeNow, accountTest1);
		Transaction transaction4 = new Transaction(TransactionType.CREDIT, 20000, "Transferencia de Cosme fulanito", dateTimeNow, accountTest2);
		Transaction transaction5 = new Transaction(TransactionType.DEBIT, -2000, "Transferencia a Homero", dateTimeNow, accountTest2);
		Transaction transaction6 = new Transaction(TransactionType.CREDIT, 2000, "Transferencia de March", dateTimeNow, accountTest3);
		Transaction transaction7 = new Transaction(TransactionType.DEBIT, -2000, "Transferencia a Bart", dateTimeNow.plusYears(1), accountTest3);
		Transaction transaction8 = new Transaction(TransactionType.CREDIT, 12000, "Transferencia de Lisa", dateTimeNow.plusYears(1), accountTest4);
		Transaction transaction9 = new Transaction(TransactionType.DEBIT, -2000, "Transferencia a Maggie", dateTimeNow.plusYears(1), accountTest4);
		Transaction transaction10 = new Transaction(TransactionType.DEBIT, -2000, "Transferencia a Homero J", dateTimeNow.plusYears(1), accountTest4);
		transactionRepository.save(transaction1);
		transactionRepository.save(transaction2);
		transactionRepository.save(transaction3);
		transactionRepository.save(transaction4);
		transactionRepository.save(transaction5);
		transactionRepository.save(transaction6);
		transactionRepository.save(transaction7);
		transactionRepository.save(transaction8);
		transactionRepository.save(transaction9);
		transactionRepository.save(transaction10);
	}

	private void createLoans(Client melbaClient, Client gusTavoClient, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		List<Integer> payments = List.of(12,24,36,48,60);
		Loan hipotecaryLoan = new Loan("Hipotecario", 500000, payments);
		loanRepository.save(hipotecaryLoan);

		Loan personalLoan = new Loan("Personal", 100000, List.of(6,12,24));
		loanRepository.save(personalLoan);

		payments = List.of(6,12,24,36);
		Loan prendaryLoan = new Loan("Automotriz", 300000, payments);
		loanRepository.save(prendaryLoan);

		this.createClientLoans(melbaClient, gusTavoClient,hipotecaryLoan,personalLoan,prendaryLoan,clientLoanRepository);
	}

	private void createClientLoans(Client melbaClient, Client gusTavoClient, Loan hipotecaryLoan, Loan personalLoan,
								   Loan prendaryLoan, ClientLoanRepository clientLoanRepository)
	{
		ClientLoan melbaHipotecaryLoan = new ClientLoan(400000,60,melbaClient,hipotecaryLoan);
		clientLoanRepository.save(melbaHipotecaryLoan);

		ClientLoan melbaPersonalLoan = new ClientLoan(50000,12,melbaClient,personalLoan);
		clientLoanRepository.save(melbaPersonalLoan);

		ClientLoan gusTavoPersonalLoan = new ClientLoan(100000,24,gusTavoClient,personalLoan);
		clientLoanRepository.save(gusTavoPersonalLoan);

		ClientLoan gusTavoPrendaryLoan = new ClientLoan(200000,36,gusTavoClient,prendaryLoan);
		clientLoanRepository.save(gusTavoPrendaryLoan);

	}

	private void createCards(Client melbaClient, Client gusTavoClient, LocalDate localDate, CardRepository cardRepository) {
		Card melbaGoldCard = new Card(melbaClient.getFirstName() + melbaClient.getLastName(),CardType.CREDIT, CardColor.GOLD,
				"4704-1111-2222-3333", 23, localDate, localDate.plusYears(5), melbaClient);
		Card melbaSilverDebitCard = new Card(melbaClient.getFirstName() + " " + melbaClient.getLastName(),
				CardType.DEBIT, CardColor.SILVER,
				"4704-8484-2424-1234", 993, localDate, localDate.plusYears(-5), melbaClient);
		Card melbaTitaniumCard = new Card(melbaClient.getFirstName() + melbaClient.getLastName(),CardType.CREDIT, CardColor.TITANIUM,
				"4704-4444-5555-6666", 456, localDate, localDate.plusYears(-5), melbaClient);
		Card gusTavoSilverCard = new Card(gusTavoClient.getFirstName() + gusTavoClient.getLastName(),CardType.CREDIT, CardColor.SILVER,
				"4704-7777-8888-9999", 789, localDate, localDate.plusYears(5), gusTavoClient);
		cardRepository.save(melbaGoldCard);
		cardRepository.save(melbaSilverDebitCard);
		cardRepository.save(melbaTitaniumCard);
		cardRepository.save(gusTavoSilverCard);
	}

}
