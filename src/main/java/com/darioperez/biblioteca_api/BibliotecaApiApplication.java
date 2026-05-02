package com.darioperez.biblioteca_api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApiApplication {

	public static void main(String[] args) {
		try {
			Dotenv dotenv = Dotenv.load();

			setSystemPropertyIfPresent(dotenv, "DB_URL");
			setSystemPropertyIfPresent(dotenv, "DB_USER");
			setSystemPropertyIfPresent(dotenv, "DB_PASSWORD");
			setSystemPropertyIfPresent(dotenv, "JWT_SECRET");
			setSystemPropertyIfPresent(dotenv, "OPEN_ROUTER_KEY");
			setSystemPropertyIfPresent(dotenv, "OPENROUTER_MODEL");
		} catch (Exception e) {

		}

		SpringApplication.run(BibliotecaApiApplication.class, args);
	}

	private static void setSystemPropertyIfPresent(Dotenv dotenv, String key) {
		String value = dotenv.get(key);
		if (value != null && !value.isBlank()) {
			System.setProperty(key, value);
		}
	}

}
