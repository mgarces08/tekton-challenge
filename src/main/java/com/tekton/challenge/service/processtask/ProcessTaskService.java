package com.tekton.challenge.service.processtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekton.challenge.ChallengeApplication;
import com.tekton.challenge.domain.FailedAsyncTask;
import com.tekton.challenge.domain.enumeration.ProcessStatus;
import com.tekton.challenge.domain.enumeration.ProcessType;
import com.tekton.challenge.repository.FailedAsyncTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProcessTaskService {

  private static final int MAX_RETRIES = 3;
  private static final Logger LOG = LoggerFactory.getLogger(ChallengeApplication.class);
  private final FailedAsyncTaskRepository failedAsyncTaskRepository;
  private final Map<ProcessType, ProcessTask> processTaskMap;
  private final ObjectMapper mapper;

  public ProcessTaskService(List<ProcessTask> processTasks, FailedAsyncTaskRepository failedAsyncTaskRepository, ObjectMapper mapper) {
      this.processTaskMap = processTasks.stream()
                                        .collect(Collectors.toMap(ProcessTask::getType, Function.identity()));
      this.failedAsyncTaskRepository = failedAsyncTaskRepository;
      this.mapper = mapper;
  }

  public void process() {
    List<FailedAsyncTask> tasks = failedAsyncTaskRepository.findByRetriesLessThan(MAX_RETRIES);

    for (FailedAsyncTask task : tasks) {
      try {
        ProcessTask processTask = processTaskMap.get(task.getProcess());
        ProcessData data = mapper.readValue(task.getData(), ProcessData.class);

        processTask.process(data);

        failedAsyncTaskRepository.delete(task);
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        task.setRetries(task.getRetries() + 1);
        if (task.getRetries() > MAX_RETRIES) {
          task.setStatus(ProcessStatus.FAIL);
        }
        failedAsyncTaskRepository.save(task);
      }
    }
  }
}
