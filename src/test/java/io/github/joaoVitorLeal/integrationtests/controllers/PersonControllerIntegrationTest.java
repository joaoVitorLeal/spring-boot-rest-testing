package io.github.joaoVitorLeal.integrationtests.controllers;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.joaoVitorLeal.config.TestConfigs;
import io.github.joaoVitorLeal.integrationtests.testcontainers.AbstractIntegrationTest;
import io.github.joaoVitorLeal.model.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // Carrega o contexto completo e sobe o servidor na porta definida
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
public class PersonControllerIntegrationTest extends AbstractIntegrationTest {
	
	// Config do RestAssured
	private static RequestSpecification specification; // Requisições REST definidas com RestAssured
	
	private static ObjectMapper mapper;
	
	private static Person person0;
	
	@BeforeAll
	public static void setup() {
		
		// Given
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  // Ignora campos desconhecidos ao desserializar JSON
		
		specification = new RequestSpecBuilder()
				.setBasePath("/person")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)) // Filtro para logar todas as informações da request
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // Filtro para logar todas as informações da response
				.build();
		
		person0 = new Person(
				"João",
				"Castro",
				"joaoleal98@outlook.com",
				"Salvador - BA - Brasil",
				"Male"
			);
	}
	
	@Test
	@Order(1)
	@DisplayName("JUnit Integration test - Given a valid Person object, when create a Person via POST, then returns a created Person with all fields set")
	void givenPersonObject_whenCreatePerson_thenReturnsCreatedPerson() throws JsonMappingException, JsonProcessingException {
	    // Given: uma instância de Person (person0) pronta para ser enviada na requisição
	    //         e a especificação (specification) já configurada com baseURL, porta, autenticação etc.
	    var content = given()
	            .spec(specification)
	            .contentType(TestConfigs.CONTENT_TYPE_JSON) // Define que o corpo da requisição será JSON
	            .body(person0)                              // Serializa o objeto person0 para JSON no corpo
	        // When: faz uma requisição HTTP POST para criar um novo registro de Person
	        .when()
	            .post()
	        // Then: valida que o status HTTP retornado foi 201 Created
	        .then()
	            .statusCode(201)
	        // And: extrai o corpo da resposta como String, para ser desserializado
	            .extract()
	            	.body()
	                	.asString();

	    // Converte (desserializa) o JSON de resposta em um objeto Person
	    Person createdPerson = mapper.readValue(content, Person.class);

	    // Atualiza a referência person0 com o objeto retornado (útil para os testes subsequentes)
	    person0 = createdPerson;

	    // Assertions: validações de integridade dos dados retornados
	    assertNotNull(createdPerson, "The created object must not be null");
	    assertNotNull(createdPerson.getId(), "The returned ID must not be null");
	    assertNotNull(createdPerson.getFirstName(), "firstName must not be null");
	    assertNotNull(createdPerson.getLastName(), "lastName must not be null");
	    assertNotNull(createdPerson.getAddress(), "address must not be null");
	    assertNotNull(createdPerson.getGender(), "gender must not be null");
	    assertNotNull(createdPerson.getEmail(), "email must not be null");

	    assertTrue(createdPerson.getId() > 0, "ID must be greater than zero");
	    assertInstanceOf(Person.class, createdPerson, "The returned object must be an instance of Person");

	    // Valida campos específicos conforme os dados esperados no cenário de teste
	    assertEquals("João", createdPerson.getFirstName(), "firstName should be 'João'");
	    assertEquals("Castro", createdPerson.getLastName(), "lastName should be 'Castro'");
	    assertEquals("Salvador - BA - Brasil", createdPerson.getAddress(), "address should be 'Salvador - BA - Brasil'");
	    assertEquals("Male", createdPerson.getGender(), "gender should be 'Male'");
	    assertEquals("joaoleal98@outlook.com", createdPerson.getEmail(), "email should be 'joaoleal98@outlook.com'");
	}

	@Test
	@Order(2)
	@DisplayName("JUnit Integration test - Given a valid Person object, when update a Person via PUT, then returns a updated Person")
	void givenPersonObject_whenUpdatePerson_thenReturnsUpdatedPerson() throws JsonMappingException, JsonProcessingException {
		
		person0.setFirstName("José");
		person0.setEmail("jc_engenharia@gmail.com");
		
		var content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON) 
				.body(person0)                              
			.when()
				.put()
			.then()
				.statusCode(200)
				.extract()
					.body()
						.asString();
		
		Person updatedPerson = mapper.readValue(content, Person.class);
		
		person0 = updatedPerson;
		
		assertNotNull(updatedPerson, "The created object must not be null");
		assertNotNull(updatedPerson.getId(), "The returned ID must not be null");
		assertNotNull(updatedPerson.getFirstName(), "firstName must not be null");
		assertNotNull(updatedPerson.getLastName(), "lastName must not be null");
		assertNotNull(updatedPerson.getAddress(), "address must not be null");
		assertNotNull(updatedPerson.getGender(), "gender must not be null");
		assertNotNull(updatedPerson.getEmail(), "email must not be null");
		
		assertTrue(updatedPerson.getId() > 0, "ID must be greater than zero");
		assertInstanceOf(Person.class, updatedPerson, "The returned object must be an instance of Person");
		
		assertEquals("José", updatedPerson.getFirstName(), "firstName should be 'José'");
		assertEquals("Castro", updatedPerson.getLastName(), "lastName should be 'Castro'");
		assertEquals("Salvador - BA - Brasil", updatedPerson.getAddress(), "address should be 'Salvador - BA - Brasil'");
		assertEquals("Male", updatedPerson.getGender(), "gender should be 'Male'");
		assertEquals("jc_engenharia@gmail.com", updatedPerson.getEmail(), "email should be 'jc_engenharia@gmail.com'");
	}
	
	@Test
	@Order(3)
	@DisplayName("JUnit Integration test - Given a valid Person ID, when find a Person by ID via GET, should returns a Person object")
	void givenValidPersonId_whenFindById_shouldReturnsPersonObject() throws JsonMappingException, JsonProcessingException {
		
	    var content = given()	
	    		.spec(specification)
	    		.pathParam("id", person0.getId())
        .when()
            .get("/{id}")
        .then()
            .statusCode(200)
            .extract()
            	.body()
            		.asString();

	    Person foundPerson = mapper.readValue(content, Person.class);

	    assertNotNull(foundPerson, "The created object must not be null");
	    assertNotNull(foundPerson.getId(), "The returned ID must not be null");
	    assertNotNull(foundPerson.getFirstName(), "firstName must not be null");
	    assertNotNull(foundPerson.getLastName(), "lastName must not be null");
	    assertNotNull(foundPerson.getAddress(), "address must not be null");
	    assertNotNull(foundPerson.getGender(), "gender must not be null");
	    assertNotNull(foundPerson.getEmail(), "email must not be null");

	    assertTrue(foundPerson.getId() > 0, "ID must be greater than zero");
	    assertInstanceOf(Person.class, foundPerson, "The returned object must be an instance of Person");

	    assertEquals("José", foundPerson.getFirstName(), "firstName should be 'José'");
	    assertEquals("Castro", foundPerson.getLastName(), "lastName should be 'Castro'");
	    assertEquals("Salvador - BA - Brasil", foundPerson.getAddress(), "address should be 'Salvador - BA - Brasil'");
	    assertEquals("Male", foundPerson.getGender(), "gender should be 'Male'");
	    assertEquals("jc_engenharia@gmail.com", foundPerson.getEmail(), "email should be 'jc_engenharia@gmail.com'");
	}
	
	@Test
	@Order(4)
	@DisplayName("JUnit Integration test - Given a list of Person, when find all via GET, should returns a list of Person object")
	void givenPersonList_whenFindAll_shouldReturnsAllPersons() throws JsonMappingException, JsonProcessingException {
	
		Person person1 = new Person(
				"Tânia",
				"Leal",
				"tan@hotmail.com", 
				"Paulo Afonso - BA - Brasil", 
				"Female"
			);
		
		// Persist a new person 
	    given()
           .spec(specification)
           .contentType(TestConfigs.CONTENT_TYPE_JSON)
           .body(person1)
        .when()
            .post()
        .then()
            .statusCode(201);
		
	    // find all persons
	    var content = given()
	            .spec(specification)
	        .when()
	            .get()
	        .then()
	            .statusCode(200)
	            .extract()
	            	.body()
	            		.asString();
	    
	    // Deserialize JSON array into a list of Person objects
	    Person[] personArray = mapper.readValue(content, Person[].class);
	    List<Person> personList= Arrays.asList(personArray);
	    
	    // Validate fields of the first person in the list
	    Person foundPersonOne = personList.get(0);

	    assertNotNull(foundPersonOne, "The created object must not be null");
	    assertNotNull(foundPersonOne.getId(), "The returned ID must not be null");
	    assertNotNull(foundPersonOne.getFirstName(), "firstName must not be null");
	    assertNotNull(foundPersonOne.getLastName(), "lastName must not be null");
	    assertNotNull(foundPersonOne.getAddress(), "address must not be null");
	    assertNotNull(foundPersonOne.getGender(), "gender must not be null");
	    assertNotNull(foundPersonOne.getEmail(), "email must not be null");
	    
	    assertTrue(foundPersonOne.getId() > 0, "ID must be greater than zero");
	    assertInstanceOf(Person.class, foundPersonOne, "The returned object must be an instance of Person");
	    
	    assertEquals("José", foundPersonOne.getFirstName(), "firstName should be 'José'");
	    assertEquals("Castro", foundPersonOne.getLastName(), "lastName should be 'Castro'");
	    assertEquals("Salvador - BA - Brasil", foundPersonOne.getAddress(), "address should be 'Salvador - BA - Brasil'");
	    assertEquals("Male", foundPersonOne.getGender(), "gender should be 'Male'");
	    assertEquals("jc_engenharia@gmail.com", foundPersonOne.getEmail(), "email should be 'jc_engenharia@gmail.com'");
	   
	    // Validate fields of the second person in the list
	    Person foundPersonTwo = personList.get(1);
	   
	    assertNotNull(foundPersonTwo, "The created object must not be null");
	    assertNotNull(foundPersonTwo.getId(), "The returned ID must not be null");
	    assertNotNull(foundPersonTwo.getFirstName(), "firstName must not be null");
	    assertNotNull(foundPersonTwo.getLastName(), "lastName must not be null");
	    assertNotNull(foundPersonTwo.getAddress(), "address must not be null");
	    assertNotNull(foundPersonTwo.getGender(), "gender must not be null");
	    assertNotNull(foundPersonTwo.getEmail(), "email must not be null");

	    assertTrue(foundPersonTwo.getId() > 0, "ID must be greater than zero");
	    assertInstanceOf(Person.class, foundPersonTwo, "The returned object must be an instance of Person");

	    assertEquals("Tânia", foundPersonTwo.getFirstName(), "firstName should be 'Tânia'");
	    assertEquals("Leal", foundPersonTwo.getLastName(), "lastName should be 'Leal'");
	    assertEquals("Paulo Afonso - BA - Brasil", foundPersonTwo.getAddress(), "address should be 'Paulo Afonso - BA - Brasil'");
	    assertEquals("Female", foundPersonTwo.getGender(), "gender should be 'Female'");
	    assertEquals("tan@hotmail.com", foundPersonTwo.getEmail(), "email should be 'tan@hotmail.com'");
	}
	
	@Test
	@Order(5)
	@DisplayName("JUnit Integration test - Given a valid Person ID, when delete a Person via DELETE, should returns No Content")
	void givenValidPersonId_whenDelete_shouldReturnsNoContent() throws JsonMappingException, JsonProcessingException {
		given()
            .spec(specification)
            .pathParam("id", person0.getId())
	    .when()
            .delete("/{id}")
	    .then()
            .statusCode(204);
	}
}
