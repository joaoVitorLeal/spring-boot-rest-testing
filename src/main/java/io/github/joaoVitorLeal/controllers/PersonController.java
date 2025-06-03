package io.github.joaoVitorLeal.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public Person findById(@PathVariable String id) throws Exception {
		return service.findById(id);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Person> findAll() throws Exception {
		return service.findAll();
	}
	
	@PostMapping(
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
		)
	public Person create(@RequestBody Person person) throws Exception {
		return service.create(person);
	}
	
	@PutMapping(
			value = "/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
		)
	public Person update(@PathVariable String id, @RequestBody Person person) throws Exception {
		return service.create(person);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) throws Exception {
		service.delete(id);
	}
}
