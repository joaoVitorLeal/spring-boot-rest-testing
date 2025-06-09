package io.github.joaoVitorLeal.repositories;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import io.github.joaoVitorLeal.integrationtests.testcontainers.AbstractIntegrationTest;
import io.github.joaoVitorLeal.model.Person;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Impedir que o Spring Boot substitua sua configuração de banco de dados real por um banco de dados em memória 
@ActiveProfiles("test")
public class PersonRepositoryTest extends AbstractIntegrationTest {
	
	@Autowired
	private PersonRepository repository;

	private Person person0;
	
	@BeforeEach
	void setup() {
		// Given / Arrange //
		person0 = new Person("João", "Castro", "joaoleal98@outlook.com", "Salvador - BA - Brasil", "Male");
	}
	
	@Test
	@DisplayName("Given person object when save() is called, then return saved person")
	void testGivenPersonObject_whenSave_thenReturnSavedPerson() {
		// Given - is in the setup()
		
		// When
		Person savedPerson = repository.save(person0);
		
		// Then
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() != null && savedPerson.getId() > 0);
		assertEquals(person0, savedPerson);
		
		assertThat(savedPerson.getFirstName(), startsWith("Jo"));
	}
	
	@Test
	@DisplayName("Given person object when findById() is called, then return the same person")
	void testGivenPersonId_whenFindById_thenReturnPersonObject() {
		// Given 
		repository.save(person0);

		// When
		Person savedPerson = repository.findById(person0.getId()).get(); 
		
		// Then
		assertNotNull(savedPerson);
		assertEquals(person0.getId(), savedPerson.getId());
	}
	
	@Test
	@DisplayName("Given person object when findByEmail() is called, then return the same person")
	void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {
		// Given
		repository.save(person0);

		// When
		Person savedPerson = repository.findByEmail(person0.getEmail()).get(); 

		// Then
		assertNotNull(savedPerson);
		
		// Usando assertThat: //
	    assertThat(savedPerson.getId(), is(person0.getId())); // compara IDs
	    assertThat(savedPerson.getEmail(), equalTo(person0.getEmail())); // compara e-mails
	    assertThat(savedPerson.getFirstName(), startsWith("Jo")); // valida prefixo do nome
	    assertThat(savedPerson.getGender(), is("Male")); // valida gênero
	    assertThat(savedPerson.getAddress(), containsString("Brasil")); // valida que o endereço contém "Brasil"
	    assertThat(savedPerson, notNullValue()); // redundant, but expressive
	}
	
	@Test
	@DisplayName("Given person list when findAll() is called, then return all persons")
	void testGivenPersonList_whenFindAll_thenReturnPersonList() {
		// Given
		Person person1 = new Person("Manuela", "Mariano", "manuarq@gmail.com", "Belo Horizonte - MG - Brasil", "Female");
		repository.save(person0);
		repository.save(person1);

		// When
		List<Person> personList = repository.findAll();
		
		// Then
		assertNotNull(personList);
		assertEquals(2, personList.size());
		
		assertThat(personList.size(), is(2));
	}
	
	@Test
	@DisplayName("Given person object when update() is called, then return updated person")
	void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {
		// Given
		repository.save(person0);

		// When
		Person savedPerson = repository.findById(person0.getId()).get();
		savedPerson.setFirstName("Vitor");
		savedPerson.setEmail("estudosjv@gmail.com");
		
		Person updatedPerson = repository.save(savedPerson); // Act
		
		// Then
		assertNotNull(updatedPerson);
		assertEquals(person0.getId(), savedPerson.getId());
		assertEquals("Vitor", updatedPerson.getFirstName());
		assertThat(updatedPerson.getEmail(), is("estudosjv@gmail.com"));
	}
	
	@Test
	@DisplayName("JUnit test for Given person object when delete() is called, then remove person")
	void testGivenPersonObject_whenDelete_thenRemovePerson() {
		// Given
		repository.save(person0);

		// When
		repository.deleteById(person0.getId());
		Optional<Person> personOptional = repository.findById(person0.getId()); 
		
		// Then
		assertTrue(personOptional.isEmpty());
	}
	
	@Test
	@DisplayName("Given person object when findByJPQL() is called, then return the same person")
	void testGivenPersonObject_whenFindByJPQL_thenReturnPersonObject() {
		// Given
		repository.save(person0);
		
		// Data of person0
		String firstName = "João";
		String lastName = "Castro";
		
		// When
		Person savedPerson = repository.findByJPQL(firstName, lastName); 
		
		// Then
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(lastName, savedPerson.getLastName());
	}
	
	@Test
	@DisplayName("Given person object when findByJPQLNamedParameters is called, then return the same person")
	void shouldReturnPersonObject_whenFindByJPQLNamedParametersIsCalled() {
		// Given
		repository.save(person0);
		
		// Data of person0
		String firstName = "João";
		String lastName = "Castro";
		String gender = "Male";
		
		// When
		Person savedPerson = repository.findByJPQLNamedParameters(firstName, lastName, gender); 
		
		// Then
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(lastName, savedPerson.getLastName());
		assertEquals(gender, savedPerson.getGender());
	}
	
	@Test
	@DisplayName("Given person object when findByNativeSQL is called, then return the same person")
	void shouldReturnPersonObject_whenFindByNativeSQLIsCalled() {
		// Given
		repository.save(person0);
		
		String firstName = "João"; // First name of person0
		String email = "joaoleal98@outlook.com"; // Email of person0
		
		// When
		Person savedPerson = repository.findByNativeSQL(firstName, email); 
		
		// Then
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(email, savedPerson.getEmail());
	}
	
	@Test
	@DisplayName("Given person object when findByNativeSQLWithNamedParameters is called, then return the same person")
	void shouldReturnPersonObject_whenFindByNativeSQLWithNamedParameters_isCalled() {
		// Given
		String firstName = "João";
		String email = "joaoleal98@outlook.com";
		
		repository.save(person0);
		
		// When
		Person savedPerson = repository.findByNativeSQLWithNamedParameters(firstName, email); 
		
		// Then
		assertNotNull(savedPerson);
		assertEquals(firstName, savedPerson.getFirstName());
		assertEquals(email, savedPerson.getEmail());
	}
}
