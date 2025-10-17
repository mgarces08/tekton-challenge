package com.tekton.challenge.service.processtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekton.challenge.domain.FailedAsyncTask;
import com.tekton.challenge.domain.enumeration.ProcessStatus;
import com.tekton.challenge.domain.enumeration.ProcessType;
import com.tekton.challenge.repository.FailedAsyncTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessTaskServiceTest {

  @Mock
  private FailedAsyncTaskRepository failedAsyncTaskRepository;
  @Mock
  private ProcessTask processTask;
  private ObjectMapper mapper;

  @BeforeEach
  void setup() {
    mapper = new ObjectMapper();
  }

  @Test
  void testProcessSuccessDeletesTask() throws Exception {
    FailedAsyncTask task = new FailedAsyncTask();
    task.setId(1L);
    task.setProcess(ProcessType.SAVE_HISTORY);
    task.setRetries(0);
    String[] params = {"/api/calc","{}", "resp"};
    task.setData(new ProcessData(params).toString());

    when(failedAsyncTaskRepository.findByRetriesLessThan(3)).thenReturn(List.of(task));
    when(processTask.getType()).thenReturn(ProcessType.SAVE_HISTORY);

    ProcessTaskService svc = new ProcessTaskService(
        List.of(processTask), failedAsyncTaskRepository, mapper);

    svc.process();

    verify(processTask).process(any(ProcessData.class));
    verify(failedAsyncTaskRepository).delete(task);
    verify(failedAsyncTaskRepository, never()).save(any());
    verifyNoMoreInteractions(processTask);
    verifyNoMoreInteractions(failedAsyncTaskRepository);
  }

  @Test
  void tetsProcessFailureIncrementsRetriesAndPersists() throws Exception {
    FailedAsyncTask task = new FailedAsyncTask();
    task.setId(2L);
    task.setProcess(ProcessType.SAVE_HISTORY);
    task.setRetries(1);
    String[] params = {"/api/calc","{}", "r"};
    task.setData(new ProcessData(params).toString());

    when(failedAsyncTaskRepository.findByRetriesLessThan(3)).thenReturn(List.of(task));

    when(processTask.getType()).thenReturn(ProcessType.SAVE_HISTORY);
    doThrow(new RuntimeException("error")).when(processTask).process(any());

    ProcessTaskService svc = new ProcessTaskService(List.of(processTask), failedAsyncTaskRepository, mapper);
    svc.process();

    assertThat(task.getRetries()).isEqualTo(2);
    verify(failedAsyncTaskRepository).save(task);
    assertThat(task.getStatus()).isNotEqualTo(ProcessStatus.FAIL);
    verifyNoMoreInteractions(failedAsyncTaskRepository);
  }

  @Test
  void testProcessFailureReachesMaxMarksFail() throws Exception {
    FailedAsyncTask task = new FailedAsyncTask();
    task.setId(3L);
    task.setProcess(ProcessType.SAVE_HISTORY);
    task.setRetries(3);
    String[] params = {"/api/calc","{}", "r"};
    task.setData(new ProcessData(params).toString());

    when(failedAsyncTaskRepository.findByRetriesLessThan(3)).thenReturn(List.of(task));

    when(processTask.getType()).thenReturn(ProcessType.SAVE_HISTORY);
    doThrow(new RuntimeException("error")).when(processTask).process(any());

    ProcessTaskService svc = new ProcessTaskService(List.of(processTask), failedAsyncTaskRepository, mapper);
    svc.process();

    assertThat(task.getRetries()).isEqualTo(4);
    assertThat(task.getStatus()).isEqualTo(ProcessStatus.FAIL);
    verify(failedAsyncTaskRepository).save(task);
    verifyNoMoreInteractions(failedAsyncTaskRepository);
  }
}
