package io.github.joaoVitorLeal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("REST API - Unit and Integrations Tests")
						.version("1.0.0")
						.description("REST API using unit and integration tests ")
						.termsOfService("https://github.com/joaoVitorLeal/spring-boot-rest-testing")
						.license(new License().name("MIT License")
								.url("https://github.com/joaoVitorLeal/spring-boot-rest-testing/blob/main/LICENSE")
							)
					);
	}
}
