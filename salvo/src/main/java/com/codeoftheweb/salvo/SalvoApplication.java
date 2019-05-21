package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
		return (args) -> {

			LocalDateTime localDateTime = LocalDateTime.now();

			Player player1 = new Player("Jack", "Bauer", "j.bauer@ctu.gov", "24", "j.bauer");
			Player player2 = new Player("Chloe", "O'Brian", "c.obrian@ctu.gov", "42", "c.obrian");
			Player player3 = new Player("Kim", "Bauer", "kim_bauer@gmail.com", "kb", "kim_bauer");
			Player player4 = new Player("Tony", "Almeida", "t.almeida@ctu.gov", "mole", "t-almeida");
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);

			Game game1 = new Game(LocalDateTime.now());
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			Game game3 = new Game(LocalDateTime.now().plusHours(2));
			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);

			GamePlayer gamePlayer1 = new GamePlayer(player1, game1, localDateTime);
			GamePlayer gamePlayer2 = new GamePlayer(player2, game3, localDateTime);
			gamePlayerRepository.save(gamePlayer1);
		};
	}
}