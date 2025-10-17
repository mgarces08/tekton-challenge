package com.tekton.challenge.service.async;

import com.tekton.challenge.domain.FailedAsyncTask;
import com.tekton.challenge.domain.enumeration.ProcessStatus;
import com.tekton.challenge.service.FailedAsyncTaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersistingAsyncExceptionHandlerTest {

  @Mock
  private FailedAsyncTaskService failedAsyncTaskService;
  @InjectMocks
  private PersistingAsyncExceptionHandler handler;

  @Test
  void testHandleUncaughtExceptionPersistsFailedTaskWithStackAndData() {
    RuntimeException runtimeException = new RuntimeException("runtimeException!");
    Method method = String.class.getMethods()[0];

    Object[] params = new Object[] { "/api/calculate", "{\"id\":1}", "error" };

    ArgumentCaptor<FailedAsyncTask> captor = ArgumentCaptor.forClass(FailedAsyncTask.class);

    handler.handleUncaughtException(runtimeException, method, params);

    verify(failedAsyncTaskService).persist(captor.capture());
    FailedAsyncTask saved = captor.getValue();

    assertThat(saved.getStatus()).isEqualTo(ProcessStatus.CREATED);
    assertThat(saved.getRetries()).isEqualTo(0);
    assertThat(saved.getCreatedAt()).isNotNull();

    assertThat(saved.getError()).contains("java.lang.RuntimeException").contains("runtimeException!");

    assertThat(saved.getData()).contains("/api/calculate");
    assertThat(saved.getData()).contains("{\"id\":1}");
    assertThat(saved.getData()).contains("error");

    assertThat(saved.getProcess()).isNotNull();
  }
}
