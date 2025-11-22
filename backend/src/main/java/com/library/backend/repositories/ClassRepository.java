package com.library.backend.repositories;

import com.library.backend.entities.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long>, JpaSpecificationExecutor<Class> {

    List<Class> findByTutorId(Long tutorId);

    List<Class> findByStatus(Class.ClassStatus status);

}
