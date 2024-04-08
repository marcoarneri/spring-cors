package it.krisopea.springcors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SpringCorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCorsApplication.class, args);
	}

}
