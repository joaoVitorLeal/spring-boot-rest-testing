package io.github.joaoVitorLeal.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.joaoVitorLeal.exceptions.ResourceNotFoundException;
import io.github.joaoVitorLeal.model.Person;
import io.github.joaoVitorLeal.repositories.PersonRepository;

@Service
public class PersonService {
	
	private Logger logger = Logger.getLogger(PersonService.class.getName());
	
	private final PersonRepository repository;
	
	public PersonService(PersonRepository repository) {
		this.repository = repository;
	}
	
	public Person findById(Long id) {
		logger.info("Finding one person.");
		return repository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("No records found for this id: " + id));
	}

	public List<Person> findAll() {
		logger.info("Finding all people.");
		return repository.findAll();	
	}
	
	@Transactional
	public Person create(Person person) {
		logger.info("Creating one person.");
		return repository.save(person);
	}
	
	@Transactional
	public Person update(Person person) {
		logger.info("Updating one person.");
		
		var existingPerson = repository.findById(person.getId())
		.orElseThrow(()-> new ResourceNotFoundException("No records found for this id: " + person.getId()));
		
		existingPerson.setFirstName(person.getFirstName());
		existingPerson.setLastName(person.getLastName());
		existingPerson.setAddress(person.getAddress());
		existingPerson.setGender(person.getGender());
	
		return repository.save(existingPerson);
	}
	
	@Transactional
	public void delete(Long id) {
		logger.info("Delete one person.");
		
		Person existingPerson = repository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("No records found for this id: " + id));
		repository.delete(existingPerson);
	}
}
