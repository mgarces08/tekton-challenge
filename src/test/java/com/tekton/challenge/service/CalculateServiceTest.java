package com.tekton.challenge.service;

import com.tekton.challenge.service.exception.PercentageUnavailableException;
import com.tekton.challenge.web.dto.CalculateRequest;
import com.tekton.challenge.web.dto.CalculateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculateServiceTest {

  @Mock
  private PercentageService percentageService;
  @Mock
  private HistoryService historyService;
  @InjectMocks
  private CalculateService calculateService;

  @Test
  void testCalculateSuccessLogsHistory() {
    when(percentageService.getPercentage()).thenReturn(new BigDecimal("15"));
    CalculateRequest request = new CalculateRequest(BigDecimal.valueOf(10), BigDecimal.valueOf(20));

    CalculateResponse resp = calculateService.calculate(request);

    assertThat(resp.sum()).isEqualByComparingTo("30");
    assertThat(resp.percentage()).isEqualByComparingTo("15");
    assertThat(resp.total()).isEqualByComparingTo("34.50");

    ArgumentCaptor<String> endpoint = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> capParams = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> response = ArgumentCaptor.forClass(String.class);
    verify(historyService).save(endpoint.capture(), capParams.capture(), response.capture());
    verify(percentageService).getPercentage();
    verifyNoMoreInteractions(historyService);
    verifyNoMoreInteractions(percentageService);
    assertThat(endpoint.getValue()).isEqualTo("/api/calculate");
    assertThat(capParams.getValue()).isEqualTo(request.toString());
    assertThat(response.getValue()).isEqualTo(resp.toString());
  }

  @Test
  void testCalculatePercentageUnavailableLogsErrorAndRethrows() {
    when(percentageService.getPercentage()).thenThrow(new PercentageUnavailableException("Percentage is empty"));

    CalculateRequest request = new CalculateRequest(BigDecimal.valueOf(1), BigDecimal.valueOf(2));

    assertThatThrownBy(() -> calculateService.calculate(request))
        .isInstanceOf(PercentageUnavailableException.class)
        .hasMessageContaining("empty");

    verify(historyService).save(eq("/api/calculate"), eq(request.toString()), contains("Percentage is empty"));
    verify(percentageService).getPercentage();
    verifyNoMoreInteractions(historyService);
    verifyNoMoreInteractions(percentageService);
  }
}
