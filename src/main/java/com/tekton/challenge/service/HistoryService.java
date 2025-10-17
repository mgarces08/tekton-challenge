package com.tekton.challenge.service;

import com.tekton.challenge.domain.History;
import com.tekton.challenge.repository.HistoryRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class HistoryService {

  private final HistoryRepository historyRepository;

  public HistoryService(HistoryRepository historyRepository) {
      this.historyRepository = historyRepository;
  }

  @Async("asyncExecutor")
  public void save(String endpoint, String request, String response) {
    var history = new History().setEndpoint(endpoint)
            .setParams(request)
            .setResponse(response)
            .setCreatedAt(Instant.now());

    historyRepository.save(history);
  }

  public List<History> getAll() {
    return historyRepository.findAll();
  }
}
