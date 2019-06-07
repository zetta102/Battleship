package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ScoreRepository scoreRepository) {
		return (args) -> {

			LocalDateTime localDateTime = LocalDateTime.now();

			Player player1 = new Player("Jack", "Bauer", "j.bauer@ctu.gov", "24", "j.bauer");
			Player player2 = new Player("Chloe", "O'Brian", "c.obrian@ctu.gov", "42", "c.obrian");
			Player player3 = new Player("Kim", "Bauer", "kim_bauer@gmail.com", "kb", "kim_bauer");
			Player player4 = new Player("Tony", "Almeida", "t.almeida@ctu.gov", "mole", "t-almeida");
			Player player5 = new Player("José", "Sierra", "heiligpfeil@gmail.com", "sankt", "heiligpfeil");
			Player player6 = new Player("Rodrigo", "García", "rodrigogarcíaribeiro@gmail.com", "capfullstack", "capfullstack");

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			playerRepository.save(player5);
			playerRepository.save(player6);

			Game game1 = new Game(LocalDateTime.now());
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			Game game3 = new Game(LocalDateTime.now().plusHours(2));

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);

			GamePlayer gamePlayer1 = new GamePlayer(player1, game1, localDateTime);
			GamePlayer gamePlayer2 = new GamePlayer(player2, game1, localDateTime);
			GamePlayer gamePlayer3 = new GamePlayer(player1, game2, localDateTime);
			GamePlayer gamePlayer4 = new GamePlayer(player2, game2, localDateTime);

			Ship ship1 = new Ship(ShipType.CARRIER, new ArrayList<>(Arrays.asList("a1","a2")));
			Ship ship2 = new Ship(ShipType.PATROL_BOAT, new ArrayList<>(Arrays.asList("c2", "b2")));
			gamePlayer1.addShip(ship1);
			gamePlayer2.addShip(ship2);

			Salvo salvo1 = new Salvo(1, new ArrayList<>(Arrays.asList("b1","c1")));
			gamePlayer1.addSalvo(salvo1);

			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);

			Score score1Game1 = new Score(1, localDateTime, game1, player1);
			Score score2Game1 = new Score(0, localDateTime, game1, player2);
			Score score1Game2 = new Score(0.5, localDateTime, game2, player1);
			Score score2Game2 = new Score(0.5, localDateTime, game2, player2);
			scoreRepository.save(score1Game1);
			scoreRepository.save(score2Game1);
			scoreRepository.save(score2Game2);
			scoreRepository.save(score1Game2);
		};
	}
}