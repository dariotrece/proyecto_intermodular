package com.darioperez.biblioteca_api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApiApplication {

	public static void main(String[] args) {
		try {
			Dotenv dotenv = Dotenv.load();

			System.setProperty("DB_URL", dotenv.get("DB_URL"));
			System.setProperty("DB_USER", dotenv.get("DB_USER"));
			System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
			System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		} catch (Exception e) {

		}

		SpringApplication.run(BibliotecaApiApplication.class, args);
	}

}
