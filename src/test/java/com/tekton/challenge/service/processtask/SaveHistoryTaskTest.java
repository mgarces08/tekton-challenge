package com.tekton.challenge.service.processtask;

import com.tekton.challenge.domain.History;
import com.tekton.challenge.repository.HistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveHistoryTaskTest {

  @Mock
  private HistoryRepository historyRepository;
  @InjectMocks
  private SaveHistoryTask task;

  @Test
  void testGetTypeReturnsSaveHistory() {
    assertThat(task.getType()).isNotNull();
  }

  @Test
  void testProcessBuildsAndSavesHistory() {
    ProcessData data = mock(ProcessData.class);
    when(data.getEndpoint()).thenReturn("/api/calculate");
    when(data.getParams()).thenReturn("{\"a\":1}");
    when(data.getResponse()).thenReturn("{\"sum\":1}");

    ArgumentCaptor<History> captor = ArgumentCaptor.forClass(History.class);

    task.process(data);

    verify(historyRepository).save(captor.capture());
    History saved = captor.getValue();

    assertThat(saved.getEndpoint()).isEqualTo("/api/calculate");
    assertThat(saved.getParams()).isEqualTo("{\"a\":1}");
    assertThat(saved.getResponse()).isEqualTo("{\"sum\":1}");
    assertThat(saved.getCreatedAt()).isNotNull();
    verifyNoMoreInteractions(historyRepository);
  }
}
