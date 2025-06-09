package io.github.joaoVitorLeal.integrationtests.testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

// Usado por todos os testes de integração para subir um banco de dados em runtime, se conectar a ele e executar as operações

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
	
	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		
		static MySQLContainer<?>  mysql = new MySQLContainer<>("mysql:8.0.41"); // subir o container do MySQL em runtime, utilizando engine do MySQL na versão especificada
		
		// Método que realiza o Stream sob o mysql container e suas configurações
		private static void startContainers() {
			Startables.deepStart(Stream.of(mysql)).join();
		}
		
		// Definir configurações de url, username e password do DB de forma dinâmica
		private static Map<String, String> createConnectionConfiguration() {
			return Map.of(
					"spring.datasource.url", mysql.getJdbcUrl(),
					"spring.datasource.username", mysql.getUsername(),
					"spring.datasource.password", mysql.getPassword()
				);
		}
		
		// Inicializar os containers
		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void initialize(ConfigurableApplicationContext applicationContext) {
			startContainers();
			ConfigurableEnvironment environment = applicationContext.getEnvironment(); // obter as configurações de ambiente do contexto da aplicação
			
			// Setar novas configurações de ambiente para conexão (url, username, password) no contexto da aplicação
			MapPropertySource testcontainers = new MapPropertySource("testcontainers",(Map) createConnectionConfiguration());
			
			environment.getPropertySources().addFirst(testcontainers);
		}
	}
}
