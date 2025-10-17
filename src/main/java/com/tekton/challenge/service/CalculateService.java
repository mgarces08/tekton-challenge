package com.tekton.challenge.service;

import com.tekton.challenge.service.exception.PercentageUnavailableException;
import com.tekton.challenge.web.dto.CalculateRequest;
import com.tekton.challenge.web.dto.CalculateResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculateService {

  private final PercentageService percentageService;
  private final HistoryService historyService;

  public CalculateService(PercentageService percentageService, HistoryService historyService) {
    this.percentageService = percentageService;
      this.historyService = historyService;
  }

  public CalculateResponse calculate(CalculateRequest body) {
    try {
      BigDecimal percentage = percentageService.getPercentage();
      BigDecimal sum = body.num1().add(body.num2());
      BigDecimal total = sum.add(sum.multiply(percentage)
                                    .divide(BigDecimal.valueOf(100)));

      CalculateResponse response = new CalculateResponse(sum, percentage, total.setScale(2, RoundingMode.HALF_UP));

      historyService.save("/api/calculate", body.toString(), response.toString());

      return response;

    } catch (PercentageUnavailableException ex) {
      historyService.save("/api/calculate", body.toString(), ex.getMessage());
      throw ex;
    }
  }
}
