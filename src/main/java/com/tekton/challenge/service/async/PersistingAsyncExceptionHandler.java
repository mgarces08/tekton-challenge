package com.tekton.challenge.service.async;

import com.tekton.challenge.domain.FailedAsyncTask;
import com.tekton.challenge.domain.enumeration.ProcessStatus;
import com.tekton.challenge.domain.enumeration.ProcessType;
import com.tekton.challenge.service.FailedAsyncTaskService;
import com.tekton.challenge.service.processtask.ProcessData;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.time.Instant;

@Component
public class PersistingAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

  private final FailedAsyncTaskService service;

  public PersistingAsyncExceptionHandler(FailedAsyncTaskService service) {
    this.service = service;
  }

  @Override
  public void handleUncaughtException(Throwable ex, Method method, Object... params) {
    FailedAsyncTask failedAsyncTask = new FailedAsyncTask();
    ProcessType processType = ProcessType.findByPath(params[0].toString());
    failedAsyncTask.setCreatedAt(Instant.now());
    failedAsyncTask.setRetries(0);
    failedAsyncTask.setStatus(ProcessStatus.CREATED);
    failedAsyncTask.setProcess(processType);
    failedAsyncTask.setError(getStack(ex));
    failedAsyncTask.setData(buildData(params));
    service.persist(failedAsyncTask);
  }

  private String buildData(Object[] params) {
    return new ProcessData(params).toString();
  }

  private String getStack(Throwable ex) {
    StringWriter sw = new StringWriter();
    ex.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }
}
