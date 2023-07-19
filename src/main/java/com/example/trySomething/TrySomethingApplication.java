package com.example.trySomething;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class TrySomethingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrySomethingApplication.class, args);
	}

}
 class FakePersonDataAccessService implements PersonOperationInterface{
	private static List<PersonModelClass> fakeDatabase =new ArrayList<>();
	 @Override
	 public int insertPerson(UUID userId, PersonModelClass personInstance) {
		 fakeDatabase.add(new PersonModelClass(userId,personInstance.getUserName()));
		 return 1;
	 }
 }
 interface PersonOperationInterface{
	int insertPerson(UUID userId,PersonModelClass personInstance);
	default int insertPerson(PersonModelClass personInstance){
		UUID generatedId = UUID.randomUUID();
		return insertPerson(generatedId,personInstance);
	}
 }
 class PersonModelClass{
	final  UUID userId;
	final String userName;


	 PersonModelClass(UUID id, String userName) {
		 this.userId = id;
		 this.userName = userName;
	 }
	public UUID getUserId(){
		 return userId;
	}
	public String getUserName(){
		 return userName;
	}
 }
