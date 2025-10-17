package com.tekton.challenge.repository;

import com.tekton.challenge.domain.History;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class)
class HistoryRepositoryTest {

  @Autowired HistoryRepository repo;

  @Test
  void testSaveAndFindAll() {
    History h = new History()
        .setEndpoint("/api/calculate")
        .setParams("{\"num1\":1,\"num2\":2}")
        .setResponse("{\"sum\":3}")
        .setCreatedAt(Instant.now());
    repo.save(h);

    List<History> all = repo.findAll();
    assertThat(all).hasSize(1);
    assertThat(all.get(0).getEndpoint()).isEqualTo("/api/calculate");
  }
}
