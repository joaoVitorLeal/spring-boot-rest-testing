package io.github.joaoVitorLeal.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.joaoVitorLeal.exceptions.DuplicateRegistrationException;
import io.github.joaoVitorLeal.model.Person;
import io.github.joaoVitorLeal.repositories.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	@Mock
	private PersonRepository repository;
	
	@InjectMocks
	private PersonService service; // Aqui será injetado o Mock de PersonRepository
	
	private Person person0;
	
	@BeforeEach
	void setup() {
		// Given / Arrange //
		person0 = new Person("João", "Castro", "joaoleal98@outlook.com", "Salvador - BA - Brasil", "Male");
	}
	
	@Test
	@DisplayName("Should return Person object when save the same Person")
	void shouldReturnPersonObject_whenSavePerson() {
		// Given
		given(repository.findByEmail(anyString())).willReturn(Optional.empty()); // when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
		given(repository.save(person0)).willReturn(person0);
		
		// When
		Person savedPerson = service.create(person0);
		
		// Then
		assertNotNull(savedPerson);
		assertEquals("João", savedPerson.getFirstName());
	}
	
	@Disabled("DIDATIC MODEL OF THROWABLE EXCEPTIONS")
	@Test
	@DisplayName("Should throw DuplicateRegistrationException when saving a Person with existing email and never call save")
	void shouldThrowDuplicateRegistrationException_whenSavingPersonWithExistingEmail_andNeverCallSave() {
	    
	    // --- Given ---
	    // Mock configurado para retornar uma Person existente ao buscar por qualquer email
	    given(repository.findByEmail(anyString()))
	        .willReturn(Optional.of(person0));
	    
	    // --- When / Then ---
	    // Espera-se que a tentativa de criar a mesma Person lance uma DuplicateRegistrationException
	    assertThrows(DuplicateRegistrationException.class, () -> {
	        service.create(person0);
	    });
	    
	    // --- And ---
	    // Verifica que o método save nunca foi chamado após a detecção da duplicação
	    verify(repository, never()).save(any(Person.class));
	}
	
	@Test
	@DisplayName("Should throw DuplicateRegistrationException and never call save when creating Person with existing email")
	void shouldThrowDuplicateRegistrationException_andNeverCallSave_whenCreatingPersonWithExistingEmail() {
		// Given
		given(repository.findByEmail(anyString())).willReturn(Optional.of(person0));
		
		// When / Then
		assertThrows(DuplicateRegistrationException.class, () -> {
			service.create(person0);
		});
		
		// And
		verify(repository, never()).save(any(Person.class));
	}
	
	@Test
	@DisplayName("Verify exception message, when create a Person with existing email and throw DuplicatedRegistrationException")
	void testCreatePerson_whenThrowDuplicatedRegistrationException_ThenReturnCorrectMessage() {
		
		// Given
		when(repository.findByEmail("joaoleal98@outlook.com")).thenReturn(Optional.of(person0));
		String expectedMessage = "Person already exist with given email: " + person0.getEmail();
		
		// When / Then
		DuplicateRegistrationException exception = assertThrows(DuplicateRegistrationException.class, 
				() -> service.create(person0),
				"A duplicated email should have caused an DuplicateRegistrationException!"
			); 
		
		assertEquals(expectedMessage, exception.getMessage(), () -> "Unexpected exception message!");
	}
	
	@Test
	@DisplayName("Given empty persons list when findAll persons, then return an empty persons list")
	void testGivenEmptyPersonsList_whenFindAllPersons_thenReturnEmptyPersonsList() {
	    // Given
	    given(repository.findAll()).willReturn(Collections.emptyList());
	    
	    // When
	    List<Person> persons = service.findAll();
	    
	    // Then
	    assertTrue(persons.isEmpty());
	    assertEquals(0, persons.size());
	}

	@Test
	@DisplayName("Given personId when findById() is called, then return the same person")
	void testGivenPersonId_whenFindById_thenReturnPersonObject() {
		
		// Given 
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));

		// When
		Person savedPerson = service.findById(1L); 
		
		// Then
		assertNotNull(savedPerson);
		assertEquals("João", savedPerson.getFirstName());
	}
	
	@Test
	@DisplayName("Given person objetc when update, then return updated person")
	void testGivenPersonObject_whenUpdate_thenReturnUpdatedPersonObject() {
		
		// Given 
		person0.setId(1L);
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
		
		person0.setFirstName("Carlos");
		person0.setEmail("carloslealurb@gmail.com");
		
		given(repository.save(person0)).willReturn(person0);
		
		// When
		Person updatedPerson = service.update(person0); 
		
		// Then
		assertNotNull(updatedPerson);
		assertEquals("Carlos", updatedPerson.getFirstName());
		assertEquals("carloslealurb@gmail.com", updatedPerson.getEmail());
		
	}
	
	@Test
	@DisplayName("Given personId when delete Person, then do nothing")
	void testGivenPersonId_whenDelete_thenDoNothing() {
		
		// Given 
		person0.setId(1L);
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
		willDoNothing().given(repository).delete(person0);; // willDoNothing -> configura o mock para não realizar nenhuma ação ao chamar delete
		
		// When
		service.delete(person0.getId()); 
		
		// Then
		verify(repository, times(1)).delete(person0);
	}
	
	@Test
	@DisplayName("Given personId when delete Person, then verify repository.delete is called")
	void testGivenPersonId_whenDelete_thenVerifyDeleteOnRepository_isCalled() {
		
		// Given 
		person0.setId(1L);
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
		
		// When
		service.delete(person0.getId()); 
		
		// Then
		verify(repository, times(1)).delete(person0);
	}
}
