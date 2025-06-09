package io.github.joaoVitorLeal.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.github.joaoVitorLeal.config.TestConfigs;
import io.github.joaoVitorLeal.integrationtests.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // Utilizar a porta especificada no application-test.yml
@ActiveProfiles("test")
public class SwaggerIntegrationTest extends AbstractIntegrationTest { // É necessário extender a classe para obter a conexão com MySQL do testcontainers
	
	@Test
	@DisplayName("JUnit test - Should display Swagger UI page")
	void testShouldDisplaySwaggerUiPage() {
		
		// Given - Dado o seguinte endereço
		var content  = given() 				// given() -> Realizar uma requisição para aplicação e retorna o resultado como uma String 
			.basePath("/swagger-ui/index.html")
			.port(TestConfigs.SERVER_PORT)
		// When - Quando realiza requisição do tipo GET para o endereço especificado (basePath, port)
			.when()
				.get()
		// Then - Então retorne o HTTP status code 200
			.then()
				.statusCode(200) 
		// And (Then) - E extraia o corpo da resposta como uma String
			.extract()   
				.body()
					.asString();
		
		assertTrue(content.contains("Swagger UI"));
	}
}
