package com.tekton.challenge.service;

import com.tekton.challenge.domain.FailedAsyncTask;
import com.tekton.challenge.repository.FailedAsyncTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FailedAsyncTaskServiceTest {

  @Mock
  private FailedAsyncTaskRepository failedAsyncTaskRepository;
  @InjectMocks
  private FailedAsyncTaskService failedAsyncTaskService;

  @Test
  void persist_callsRepository() {
    FailedAsyncTask t = new FailedAsyncTask();
    failedAsyncTaskService.persist(t);

    verify(failedAsyncTaskRepository).save(t);
    verifyNoMoreInteractions(failedAsyncTaskRepository);
  }
}
