package com.klef.jfsd.springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.klef.jfsd.springboot.model.Register;

public interface StudentRepo extends JpaRepository<Register, Integer> {

	@Query("select register from Register register where (register.id = ?1 or register.name =?2)And register.password=?3")
	Register IdOrNameAndPassword(String id, String name, String password);

}
