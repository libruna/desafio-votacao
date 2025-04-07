package com.desafio.code;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Desafio votação", version = "1.0.0", description = "API de gerenciamento de votos"))
public class VotingManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingManagerApplication.class, args);
	}
}
