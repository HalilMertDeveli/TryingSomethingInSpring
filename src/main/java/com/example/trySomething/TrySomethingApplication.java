package com.example.trySomething;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootApplication
public class TrySomethingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrySomethingApplication.class, args);
    }

}

class PersonModel {
    private final UUID personId;
    private final String personName;

    public PersonModel(@JsonProperty("id") UUID personId, @JsonProperty("personName") String personName) {
        this.personId = personId;
        this.personName = personName;
    }

    public String getPersonName() {
        return personName;
    }

    public UUID getPersonId() {
        return personId;
    }
}
interface PersonInterface{
    int addNewPerson(UUID id,PersonModel personModel);
    default int addNewPerson(PersonModel personModel){
        UUID uid = UUID.randomUUID();
        return addNewPerson(uid,personModel);
    }
    List<PersonModel> getAllPerson();

    Optional<PersonModel> getPersonById(UUID id);
}
@Repository("halil")
class DataAccessService implements  PersonInterface{

    private List<PersonModel> databaseList = new ArrayList<>();

    @Override
    public int addNewPerson(UUID id, PersonModel personModel) {
        databaseList.add(new PersonModel(id,personModel.getPersonName()));
        return 1;
    }

    @Override
    public List<PersonModel> getAllPerson() {
        return databaseList;
    }

    @Override
    public Optional<PersonModel> getPersonById(UUID id) {
        return databaseList.stream().filter(personModel -> personModel.getPersonId().equals(id)).findFirst();
    }
}
@Service
class PersonService{
  private final PersonInterface personInterface;
  @Autowired
  public PersonService(@Qualifier("halil") PersonInterface personInterface){
      this.personInterface=personInterface;

  }
  public int addNewPerson(PersonModel personModel){
      return personInterface.addNewPerson(personModel);
  }
  public List<PersonModel> getAllUser(){
      return personInterface.getAllPerson();
  }
  public Optional<PersonModel> getUserById(UUID personId){
      return personInterface.getPersonById(personId);
  }
}
@RequestMapping("api/v1/person")
@RestController
class PersonController{
    private final PersonService personService;
    @Autowired
    public PersonController(PersonService personService){
        this.personService = personService;
    }
    @PostMapping
    public int addNewPerson(@RequestBody PersonModel personModel){
        return personService.addNewPerson(personModel);
    }
    @GetMapping
    public List<PersonModel> getAllPerson(){
        return personService.getAllUser();
    }
    @GetMapping(path = "/{id}")
    public PersonModel getPersonById(@PathVariable("id") UUID userId){
        return personService.getUserById(userId).orElse(null);
    }
}
