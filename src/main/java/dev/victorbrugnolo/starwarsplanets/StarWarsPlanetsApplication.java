package dev.victorbrugnolo.starwarsplanets;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StarWarsPlanetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarWarsPlanetsApplication.class, args);
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info().title("Star Wars Planets API").version("1.0"));
	}

}
