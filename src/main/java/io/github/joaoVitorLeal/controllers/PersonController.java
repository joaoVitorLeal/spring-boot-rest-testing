package io.github.joaoVitorLeal.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.github.joaoVitorLeal.model.Person;
import io.github.joaoVitorLeal.services.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {
	
	private final PersonService service;
	
	public PersonController(PersonService service) {
		this.service = service;
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Person findById(@PathVariable Long id) {
		return service.findById(id);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Person> findAll() {
		return service.findAll();
	}
	
	@PostMapping(
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
		)
	public ResponseEntity<Person> create(@RequestBody Person person) {
		var persistedPerson = service.create(person);
		
	    URI uri = ServletUriComponentsBuilder
	            .fromCurrentRequest() // pega a URI atual, ou seja, "/person"
	            .path("/{id}")        // adiciona o ID na URI
	            .buildAndExpand(persistedPerson.getId()) // substitui {id} pelo valor real
	            .toUri();
		
		return ResponseEntity.created(uri).body(persistedPerson);
	}
	
	@PutMapping(
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
		)
	public Person update(@RequestBody Person person) {
		return service.create(person);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
