package projects.gabeross.fullstackdemo.repositories;

import org.springframework.data.repository.CrudRepository;
import projects.gabeross.fullstackdemo.models.Person;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findAll();

    List<Person> findByName(String name);
}
