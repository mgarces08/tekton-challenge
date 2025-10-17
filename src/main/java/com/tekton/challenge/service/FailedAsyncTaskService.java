package com.tekton.challenge.service;

import com.tekton.challenge.domain.FailedAsyncTask;
import com.tekton.challenge.repository.FailedAsyncTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FailedAsyncTaskService {
  private final FailedAsyncTaskRepository failedAsyncTaskRepository;

  public FailedAsyncTaskService(FailedAsyncTaskRepository failedAsyncTaskRepository){
      this.failedAsyncTaskRepository = failedAsyncTaskRepository;
  }

  @Transactional
  public void persist(FailedAsyncTask failedAsyncTask) {
    failedAsyncTaskRepository.save(failedAsyncTask);
  }
}
