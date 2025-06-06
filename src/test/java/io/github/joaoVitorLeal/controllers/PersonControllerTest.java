package io.github.joaoVitorLeal.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.joaoVitorLeal.exceptions.ResourceNotFoundException;
import io.github.joaoVitorLeal.model.Person;
import io.github.joaoVitorLeal.services.PersonService;



/**
 * Test class for {@link PersonController}.
 *
 * Utilizamos @WebMvcTest para carregar apenas os componentes da camada web, 
 * como controllers, filtros e conversores, sem carregar o contexto completo da aplicação.
 */
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    /**
     * A instância de MockMvc é injetada automaticamente para simular requisições HTTP 
     * e validar o comportamento da camada web sem a necessidade de subir um servidor real.
     */
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper mapper;
    
    /**
     * @MockitoBean substitui o PersonService no ApplicationContext por um mock do Mockito.
     */
    @MockitoBean
    private PersonService service;

    private Person person0;

    /**
     * Configuração inicial executada antes de cada teste.
     */
    @BeforeEach
    void setup() {
    	/// Given
        person0 = new Person(
            "João",
            "Castro",
            "joaoleal98@outlook.com",
            "Salvador - BA - Brasil",
            "Male"
        );
    }
    
    /********************************************************************************************************
     * Tests
     ********************************************************************************************************/
    
    /**
     * create testing,
     * @return person0
     * */
    @Test
	@DisplayName("JUnit test - Given Person object when create Person, then return saved Person")
	void testGivenPersonObject_WhenCreatePerson_thenReturnSavedPerson() throws Exception, JsonProcessingException {
		// Given
    	given(service.create(any(Person.class)))
    		.willAnswer( (invocation) -> invocation.getArgument(0) );
    	
		// When
        ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person0)));
    	
		// Then
        response.andExpect(status().isCreated()) // Espera que o resultado seja http status Created - 201
        	.andDo(print()) // Imprimir o fluxo da requisição no console
        	.andExpect(jsonPath("$.firstName", is(person0.getFirstName())))
        	.andExpect(jsonPath("$.lastName", is(person0.getLastName())))
        	.andExpect(jsonPath("$.email", is(person0.getEmail())));
	}
    
    /**
     * findAll testing,
     * @return persons (List of Persons Object)
     * */
    @Test
	@DisplayName("JUnit test - Given list of Persons when findAll, then return personsList")
	void testGivenListOfPerson_WhenFindAllPersons_thenReturnPersonsList() throws Exception, JsonProcessingException {
		
    	// Given
    	List<Person> persons = new ArrayList<>();
    	persons.add(person0);
    	persons.add(new Person(
            "Jośe",
            "Castro",
            "jcengenharia@gmail.com",
            "Maceió - AL - Brasil",
            "Male"
        ));
    	
    	given(service.findAll()).willReturn(persons);
    	
		// When
        ResultActions response = mockMvc.perform(get("/person"));
    	
		// Then 
        response
        	.andExpect(status().isOk())
        	.andDo(print())
        	.andExpect(jsonPath("$.size()", is(persons.size())));
	}
    
    /**
     * findById testing in a negative scenario,
     * @return Not Found - HTTP status 404
     * */
    @Test
    @DisplayName("JUnit test - Given invalid personId when findById, then return HTTP status Not Found")
    void testGivenInvalidPersonId_WhenFindById_thenReturnNotFound() throws Exception, JsonProcessingException, ResourceNotFoundException {
    	// Given
    	Long personId = 1L;
    	given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
    	
    	// When
    	ResultActions response = mockMvc.perform(get("/person/{id}", personId));
    	
    	// Then 
    	response.andExpect(status().isNotFound())
    	.andDo(print());
    }
    
    /**
     * Update testing in a positive scenario,
     * @return updatedPerson
     * @return OK - HTTP status 200
     * */
    @Test
    @DisplayName("JUnit test - Given person when update, return updatedPerson")
    void testGivenPersonObject_WhenUpdate_thenReturnUpdatePersonObject() throws Exception, JsonProcessingException {
    	
    	// Given
    	Long personId = 1L;
    	given(service.findById(personId)).willReturn(person0);
    	given(service.update(any(Person.class)))
    	.willAnswer( (invocation) -> invocation.getArgument(0) );
    	
    	// When
    	Person updatedPerson = new Person(
    			"Jośe",
    			"Castro",
    			"jcengenharia@gmail.com",
    			"Maceió - AL - Brasil",
    			"Male"
    			);
    	
    	ResultActions response = mockMvc.perform(put("/person")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(mapper.writeValueAsString(updatedPerson))
    		);
    	
    	// Then 
    	response.andDo(print())
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
    	.andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
    	.andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
    }
    
    /**
     * update testing in a negative scenario
     * @return updatedPerson and, 
     * @return Not Found - HTTP status 404
     * */
    @Test
	@DisplayName("JUnit test - Given unexistent Person object when update, then return HTTP status Not Found")
	void testGivenUnexistentPersonObject_WhenUpdate_thenReturnNotFound() throws Exception, JsonProcessingException {
		
    	// Given
    	Long personId = 99L;
    	given(service.findById(personId))
    		.willThrow(new ResourceNotFoundException("No records found for this id: " + personId));
    	
    	given(service.update(any(Person.class)))
    		.willAnswer( (invocation) -> invocation.getArgument(1) );
    	
		// When
        Person updatedPerson = new Person(
		        "Jośe",
		        "Castro",
		        "jcengenharia@gmail.com",
		        "Maceió - AL - Brasil",
		        "Male"
			);
        
		ResultActions response = mockMvc.perform(put("/person")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsString(updatedPerson))
        	);
    	
		// Then 
		response
	    	.andExpect(status().isNotFound())
	    	.andDo(print());
	}
    
    /**
     * delete testing in a positive scenario
     * @return No Content - HTTP status 204
     * */
    @Test
	@DisplayName("JUnit test - Given personId when delete, then return HTTP status No Content")
	void testGivenPersonId_WhenDelete_thenReturnNoContent() throws Exception, JsonProcessingException {
		// Given
    	Long personId = 1L;
    	willDoNothing().given(service).delete(personId);
    	
		// When
		ResultActions response = mockMvc.perform(delete("/person/{id}", personId));
    	
		// Then 
		response
	    	.andExpect(status().isNoContent())
	    	.andDo(print());
	}
    
}
