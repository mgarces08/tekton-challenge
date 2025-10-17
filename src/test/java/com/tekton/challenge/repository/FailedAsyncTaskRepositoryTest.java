package com.tekton.challenge.repository;

import com.tekton.challenge.domain.FailedAsyncTask;
import com.tekton.challenge.domain.enumeration.ProcessStatus;
import com.tekton.challenge.domain.enumeration.ProcessType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class)
class FailedAsyncTaskRepositoryTest {

  @Autowired
  FailedAsyncTaskRepository repo;

  @Test
  void testFindByRetriesLessThanReturnsExpected() {
    repo.save(newValidTask(0));
    repo.save(newValidTask(1));
    repo.save(newValidTask(3));

    List<FailedAsyncTask> result = repo.findByRetriesLessThan(2);

    assertThat(result)
            .extracting(FailedAsyncTask::getRetries)
            .containsExactlyInAnyOrder(0, 1);
  }

  private FailedAsyncTask newValidTask(int retries) {
    FailedAsyncTask t = new FailedAsyncTask();
    t.setProcess(ProcessType.SAVE_HISTORY);     // usa un valor real de tu enum
    t.setError("java.lang.RuntimeException: boom");
    t.setData("{\"payload\":\"algo\"}");
    t.setStatus(ProcessStatus.FAIL);       // usa un valor real de tu enum
    t.setRetries(retries);
    t.setCreatedAt(Instant.now());
    return t;
  }
}
