package io.github.joaoVitorLeal.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.joaoVitorLeal.model.Person;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	Optional<Person> findByEmail(String email);
	
	// Define custom query using JPQL with index parameters
	@Query("select p from Person p where p.firstName =?1 and p.lastName =?2 ")
	Person findByJPQL(String firstName, String lastName);
	
	// Define custom query using JPQL with named parameters
	@Query("select p from Person p where p.firstName =:firstName and p.lastName =:lastName and p.gender =:gender")
	Person findByJPQLNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("gender") String gender);

	// Define custom query using Native SQL with index parameters
	@Query(nativeQuery = true, value = "select * from person p where p.first_name =?1 and p.email =?2 ")
	Person findByNativeSQL(String firstName, String email);
	
	// Define custom query using Native SQL with named parameters
	@Query(nativeQuery = true, value = "select * from person p where p.first_name =:firstName and p.email =:email ")
	Person findByNativeSQLWithNamedParameters(String firstName, String email);
}