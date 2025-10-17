package com.tekton.challenge.repository;

import com.tekton.challenge.domain.FailedAsyncTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedAsyncTaskRepository extends JpaRepository<FailedAsyncTask, Long> {

    List<FailedAsyncTask> findByRetriesLessThan(int retries);
}
