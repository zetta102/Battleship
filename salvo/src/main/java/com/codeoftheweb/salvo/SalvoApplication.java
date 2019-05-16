package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository) {
		return (args) -> {
			repository.save(new Player("Jack", "Bauer", "j.bauer@ctu.gov", "24", "j.bauer"));
			repository.save(new Player("Chloe", "O'Brian", "c.obrian@ctu.gov", "42", "c.obrian"));
			repository.save(new Player("Kim", "Bauer", "kim_bauer@gmail.com", "kb", "kim_bauer"));
			repository.save(new Player("Tony", "Almeida", "t.almeida@ctu.gov", "mole", "t-almeida"));
		};
	}
}
