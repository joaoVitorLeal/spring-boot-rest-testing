package io.github.joaoVitorLeal.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import io.github.joaoVitorLeal.model.Person;

//@Service
public class PersonServiceMock {
	
	private final AtomicLong counter = new AtomicLong(); // Simular o ID
	private Logger logger = Logger.getLogger(PersonServiceMock.class.getName());
	
	
	public Person findById(String id) {
		
		logger.info("Finding one person.");
		
		// Mock temporário -> Estrutura de código que sustenta o projeto enquanto outros compenentes da aplicação ainda não foram desenvolvidos
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("João");
		person.setLastName("Leal");
		person.setAddress("Salvador - Bahia - Brasil");
		person.setGender("Male");
		
		return person;
	}

	public List<Person> findAll() {
		
		logger.info("Finding all people.");
		
		List<Person> persons = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			Person person = mockPerson(i);
			persons.add(person);
		}	
		return persons;	
	}

	public Person create(Person person) {
		logger.info("Creating one person.");
		return person;
	}
	
	public Person update(Person person) {
		logger.info("Updating one person.");
		return person;
	}
	
	public void delete(String id) {
		logger.info("Delete one person.");
	}
	
	private Person mockPerson(int i) {
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Person name " + i);
		person.setLastName("Last Name " + i);
		person.setAddress("Some address in Brazil " + i);
		if (i % 2 == 0) {
			person.setGender("Male");			
		} else {
			person.setGender("Female");			
		}
		return person;
	}

}
