package com.tekton.challenge.service;

import com.tekton.challenge.domain.History;
import com.tekton.challenge.repository.HistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

  @Mock
  private HistoryRepository historyRepository;
  @InjectMocks
  private HistoryService historyService;

  @Test
  void testSavePersists() {
    historyService.save("/api/calculate", "{\"a\":1}", "{\"ok\":true}");

    ArgumentCaptor<History> captor = ArgumentCaptor.forClass(History.class);
    verify(historyRepository).save(captor.capture());
    verifyNoMoreInteractions(historyRepository);

    History history = captor.getValue();
    assertThat(history.getEndpoint()).isEqualTo("/api/calculate");
    assertThat(history.getParams()).contains("a");
    assertThat(history.getResponse()).contains("ok");
    assertThat(history.getCreatedAt()).isNotNull();
  }

  @Test
  void testGetAllReturnsAllHistories() {
    History history = new History()
            .setEndpoint("/api/a").setParams("{}").setResponse("{}").setCreatedAt(Instant.now());
    History history2 = new History()
            .setEndpoint("/api/b").setParams("{}").setResponse("{}").setCreatedAt(Instant.now());
    when(historyRepository.findAll()).thenReturn(List.of(history, history2));

    List<History> result = historyService.getAll();

    assertThat(result).containsExactly(history, history2);
    verify(historyRepository).findAll();
    verifyNoMoreInteractions(historyRepository);
  }

  @Test
  void testGetAllReturnsEmptyWhenNoData() {
    when(historyRepository.findAll()).thenReturn(List.of());

    List<History> result = historyService.getAll();

    assertThat(result).isEmpty();
    verify(historyRepository).findAll();
    verifyNoMoreInteractions(historyRepository);
  }
}
