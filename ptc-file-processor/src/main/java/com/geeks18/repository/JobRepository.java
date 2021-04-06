package com.geeks18.repository;

import com.geeks18.data.JobEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JobRepository extends CrudRepository<JobEntity, Long> {

    Optional<JobEntity> findById(long id);
}